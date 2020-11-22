package com.amboucheba.soatp2.resources.unit.UserResource;

import com.amboucheba.soatp2.models.Message;
import com.amboucheba.soatp2.models.MessageList;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserResource.class)
public class GetUserMessagesByIdTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getUserMessagesById() throws Exception {
        long userId = 1L;
        User user = new User(userId, "username", "user@email.com", new Date());

        MessageList messageList = new MessageList(Collections.emptyList());

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.any())).thenReturn(ResponseEntity.ok(messageList));

        RequestBuilder request = MockMvcRequestBuilders.get("/users/" + userId + "/messages");
        MvcResult response = mvc.perform(request).andReturn();

        int responseStatus = response.getResponse().getStatus();
        int expectedStatus = HttpStatus.OK.value();

        // Compare expected response with actual response
        assertEquals(expectedStatus, responseStatus);

        MessageList responseMessageList = objectMapper
                .readerFor(MessageList.class)
                .readValue(response.getResponse().getContentAsString());

        assertEquals(0, responseMessageList.getCount());

    }

    @Test
    public void userIdDoesNotExist() throws Exception {

        long userId = 1L;
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RequestBuilder request = MockMvcRequestBuilders.get("/users/" + userId + "/messages");
        MvcResult response = mvc.perform(request).andReturn();

        int responseStatus = response.getResponse().getStatus();
        int expectedStatus = HttpStatus.NOT_FOUND.value();

        // Compare expected response with actual response
        assertEquals(expectedStatus, responseStatus);
    }

    @Test
    public void messageServiceError() throws Exception {

        long userId = 1L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.any()))
                .thenThrow(RestClientException.class);

        RequestBuilder request = MockMvcRequestBuilders.get("/users/" + userId + "/messages");
        MvcResult response = mvc.perform(request).andReturn();

        int responseStatus = response.getResponse().getStatus();
        int expectedStatus = HttpStatus.SERVICE_UNAVAILABLE.value();

        // Compare expected response with actual response
        assertEquals(expectedStatus, responseStatus);
    }
}
