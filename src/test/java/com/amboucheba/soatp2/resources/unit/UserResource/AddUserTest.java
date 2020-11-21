package com.amboucheba.soatp2.resources.unit.UserResource;

import com.amboucheba.soatp2.exceptions.ApiException;
import com.amboucheba.soatp2.models.Message;
import com.amboucheba.soatp2.models.User;
import com.amboucheba.soatp2.repositories.UserRepository;
import com.amboucheba.soatp2.resources.UserResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserResource.class)
class AddUserTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    // Happy scenario
    void addUser() throws Exception {


        User user = new User(  "User 1", "email1@email.com");
        Long userId = 1L;
        User savedUser = new User(userId, "User 1", "email1@email.com", new Date());
        Mockito.when(userRepository.save(user)).thenReturn(savedUser);

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.post("/users" )
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user));
        MvcResult response = mvc.perform(request).andReturn();
        String response_str = response.getResponse().getContentAsString();

        assertEquals(HttpStatus.CREATED.value(), response.getResponse().getStatus());

        String location = response.getResponse().getHeader("location");

        assertTrue(location.endsWith("/users/" + userId));
    }

    @Test
    // Same thing for email
    void usernameMissing() throws Exception {
        // Not setting username
        User user = new User();
        user.setEmail("newEmail@email.com");

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.post("/users" )
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user));
        MvcResult response = mvc.perform(request).andReturn();
        String response_str = response.getResponse().getContentAsString();

        ApiException responseException = objectMapper.readerFor(ApiException.class).readValue(response_str);

        assert responseException.getStatus().equals(HttpStatus.BAD_REQUEST);

    }

    @Test
    void usernameTooShort() throws Exception {
        // username must be between 6 and 255
        User user = new User(  "User", "email@email.com");

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.post("/users" )
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user));
        MvcResult response = mvc.perform(request).andReturn();
        String response_str = response.getResponse().getContentAsString();

        ApiException responseException = objectMapper.readerFor(ApiException.class).readValue(response_str);

        assert responseException.getStatus().equals(HttpStatus.BAD_REQUEST);

    }

    @Test
    void emailWrongFormat() throws Exception {

        String email = "email@";
        User user = new User(  "User 1", email);

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.post("/users" )
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user));
        MvcResult response = mvc.perform(request).andReturn();
        String response_str = response.getResponse().getContentAsString();

        ApiException responseException = objectMapper.readerFor(ApiException.class).readValue(response_str);

        assert responseException.getStatus().equals(HttpStatus.BAD_REQUEST);
    }
}