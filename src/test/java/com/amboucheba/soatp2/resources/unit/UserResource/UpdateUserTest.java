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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserResource.class)
class UpdateUserTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    // Happy path 1 : user already exists -> update it
    void updateExistingUser() throws Exception {
        Long userId = 1L;
        User user = new User(userId, "User 1", "email@email.com", null);
        User updatedUser = new User(userId, "User 1", "updated@email.com", null);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(updatedUser);

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.put("/users/" + userId )
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user));
        MvcResult response = mvc.perform(request).andReturn();
        String response_str = response.getResponse().getContentAsString();

        String expectedResponse = objectMapper.writeValueAsString(updatedUser);

        assertEquals(expectedResponse, response_str);
        assertEquals(HttpStatus.OK.value(), response.getResponse().getStatus());
    }

    @Test
    // Happy path 2 : user does not exist -> create it
    void userDoesNotExist() throws Exception {
        Long userId = 1L;
        User user = new User(userId, "User 1", "updated@email.com", null);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(user)).thenReturn(user);

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.put("/users/" + userId )
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user));
        MvcResult response = mvc.perform(request).andReturn();

        assertEquals(HttpStatus.CREATED.value(), response.getResponse().getStatus());

        String location = response.getResponse().getHeader("location");

        assertTrue(location.endsWith("/users/" + userId));
    }

    @Test
        // Same thing for EMAIL
    void usernameMissing() throws Exception {

        Long userId = 1L;
        // Not setting username
        User user = new User();
        user.setId(userId);
        user.setEmail("email@email.com");

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.put("/users/" + userId )
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user));
        MvcResult response = mvc.perform(request).andReturn();
        String response_str = response.getResponse().getContentAsString();

        ApiException responseException = objectMapper.readerFor(ApiException.class).readValue(response_str);

        assert responseException.getStatus().equals(HttpStatus.BAD_REQUEST);

    }

    @Test
    void usernameTooShort() throws Exception {

        Long userId = 1L;
        // username must be between 6 and 255
        User user = new User( userId, "User", "Message 1", new Date());

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.put("/users/" + userId )
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user));
        MvcResult response = mvc.perform(request).andReturn();
        String response_str = response.getResponse().getContentAsString();

        ApiException responseException = objectMapper.readerFor(ApiException.class).readValue(response_str);

        assert responseException.getStatus().equals(HttpStatus.BAD_REQUEST);

    }

    @Test
        // Same thing for username
    void emailWrongFormat() throws Exception {

        Long userId = 1L;
        String text = "email@";
        User user = new User( userId, "User 1", text, new Date());

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.put("/users/" + userId )
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user));
        MvcResult response = mvc.perform(request).andReturn();
        String response_str = response.getResponse().getContentAsString();

        ApiException responseException = objectMapper.readerFor(ApiException.class).readValue(response_str);

        assert responseException.getStatus().equals(HttpStatus.BAD_REQUEST);

    }
}