package com.amboucheba.soatp2.resources.unit.UserResource;

import com.amboucheba.soatp2.models.Message;
import com.amboucheba.soatp2.models.User;
import com.amboucheba.soatp2.models.UserList;
import com.amboucheba.soatp2.repositories.UserRepository;
import com.amboucheba.soatp2.resources.UserResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserResource.class)
class GetAllTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAll() throws Exception {

        // Mock dependency response: messageRepository
        List<User> users = Arrays.asList(
                new User("u1", "t1@email.com"),
                new User("u2", "t2@email.com")
        );
        Mockito.when(userRepository.findAll()).thenReturn(users);

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.get("/users");
        MvcResult response = mvc.perform(request).andReturn();
        String response_str = response.getResponse().getContentAsString();

        String expectedResponse = objectMapper.writeValueAsString(new UserList( users));

        // Compare expected response with actual response
        assertEquals(expectedResponse, response_str);

    }
}