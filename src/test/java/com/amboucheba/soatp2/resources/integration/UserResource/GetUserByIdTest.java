package com.amboucheba.soatp2.resources.integration.UserResource;

import com.amboucheba.soatp2.SoaTp2Application;
import com.amboucheba.soatp2.models.User;
import com.amboucheba.soatp2.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SoaTp2Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql(scripts = { "classpath:schema.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:reset.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class GetUserByIdTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserRepository userRepository;

    @Sql({ "classpath:schema.sql", "classpath:data.sql" })
    @Test
    void getExistingUserById() throws Exception {
        String username = "New User";
        String email = "email@email.com";
        User userToSave = new User(username, email);
        User savedUser = userRepository.save(userToSave);

        String uri = "http://localhost:" + port + "/users/" + savedUser.getId();

        ResponseEntity<User> response = testRestTemplate.getForEntity(uri, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        User fetchedUser = response.getBody();

        assertEquals(username, fetchedUser.getUsername());
        assertEquals(email, fetchedUser.getEmail());
    }

    @Sql({ "classpath:schema.sql", "classpath:data.sql" })
    @Test
    void userDoesNotExist() throws Exception {
        // Create a user and save it
        User user = new User("User 1", "email@email.com");
        User savedUser = userRepository.save(user);

        // Delete the created user
        userRepository.deleteById(savedUser.getId());

        // try to get the deleted user
        String uri = "http://localhost:" + port + "/users/" + savedUser.getId();

        ResponseEntity<User> response = testRestTemplate.getForEntity(uri, User.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}