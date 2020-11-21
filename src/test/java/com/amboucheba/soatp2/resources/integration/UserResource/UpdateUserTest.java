package com.amboucheba.soatp2.resources.integration.UserResource;

import com.amboucheba.soatp2.SoaTp2Application;
import com.amboucheba.soatp2.models.User;
import com.amboucheba.soatp2.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = SoaTp2Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql(scripts = { "classpath:schema.sql", "classpath:data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:reset.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class UpdateUserTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserRepository userRepository;


    @Test
    void updateExistingUser() throws Exception {
        // create a message and save it
        User user = new User( "username", "email@email.com");
        User savedUser = userRepository.save(user);

        // create new message to update the first one
        String username = "User 1";
        String email = "updated@email.com";
        User newUser = new User( username, email);

        // update the first user with the second one
        // Prepare request
        String uri = "http://localhost:" + port + "/users/" + savedUser.getId();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> entity = new HttpEntity<>(newUser, headers);
        // Send request and get response
        ResponseEntity<User> response = testRestTemplate.exchange(uri, HttpMethod.PUT, entity, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        User updatedUser = response.getBody();

        assertEquals(savedUser.getId(), updatedUser.getId());
        assertEquals(username, updatedUser.getUsername());
        assertEquals(email, updatedUser.getEmail());
    }

    @Test
    // User does not exist -> create it
    void userDoesNotExist() throws Exception {
        //Create a message and save it
        User oldUser = userRepository.save(new User( "username", "email@email.com"));

        // delete the user
        userRepository.deleteById(oldUser.getId());

        // create new user to update
        String username = "User 1";
        String email = "email@email.com";
        User updatedUser = new User(username, email);

        // Prepare request
        String uri = "http://localhost:" + port + "/users/" + oldUser.getId();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> entity = new HttpEntity<>(updatedUser, headers);
        // Send request and get response
        ResponseEntity<User> response = testRestTemplate.exchange(uri, HttpMethod.PUT, entity, User.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        String location = response.getHeaders().getFirst("location");

        User createdUser = testRestTemplate.getForObject(location, User.class);

        assertEquals(username, createdUser.getUsername());
        assertEquals(email, createdUser.getEmail());

    }
}
