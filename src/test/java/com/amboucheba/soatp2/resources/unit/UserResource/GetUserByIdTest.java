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
class GetUserByIdTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    // Get user with valid id : expect a user
    void getUserById() throws Exception {

        long userId = 1;
        User user = new User(userId, "User 1", "user@email.com", new Date());

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.get("/users/" + userId);
        MvcResult response = mvc.perform(request).andReturn();
        String response_str = response.getResponse().getContentAsString();

        String expectedResponse = objectMapper.writeValueAsString( user);

        // Compare expected response with actual response
        assertEquals(expectedResponse, response_str);
    }

    @Test
    void userIdNotFound() throws Exception {
        long userId = 1;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.get("/users/" + userId);
        MvcResult response = mvc.perform(request).andReturn();
        // Response is supposed to be an ApiException
        String response_str = response.getResponse().getContentAsString();
        ApiException responseException = objectMapper.readerFor(ApiException.class).readValue(response_str);

        assert responseException.getStatus().equals(HttpStatus.NOT_FOUND);

    }
}