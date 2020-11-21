package com.amboucheba.soatp2.resources.integration.UserResource;

import com.amboucheba.soatp2.SoaTp2Application;
import com.amboucheba.soatp2.models.User;
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
class AddUserTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void addUser() throws Exception {
        String username = "User 1";
        String email = "email@email.com";
        User user = new User(username, email);

        String uri = "http://localhost:" + port + "/users";

        ResponseEntity<User> response = testRestTemplate.postForEntity(uri, user, User.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        String location = response.getHeaders().getFirst("location");
        User savedUser = testRestTemplate.getForObject(location, User.class);

        assertNotNull(savedUser.getId());
        assertNotNull(savedUser.getCreated_at());
        assertEquals(username, savedUser.getUsername());
        assertEquals(email, savedUser.getEmail());
    }
}