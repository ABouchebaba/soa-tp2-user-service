package com.amboucheba.soatp2.resources.integration.UserResource;


import com.amboucheba.soatp2.SoaTp2Application;
import com.amboucheba.soatp2.models.Message;
import com.amboucheba.soatp2.models.MessageList;
import com.amboucheba.soatp2.models.User;
import com.amboucheba.soatp2.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collections;

@SpringBootTest(classes = SoaTp2Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql(scripts = { "classpath:schema.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:reset.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class GetUserMessageByIdTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    // To mock the message service response, since the message service state is difficult to control from the user service
    private RestTemplate restTemplate;

    @Test
    public void userExists(){
        // user to get messages from
        String username = "username";
        User user = new User( username, "user@email.com");
        user = userRepository.save(user); // save to avoid 404

        // Mocking response from message service
        String messageServiceUri = "https://amboucheba-soa-tp2.herokuapp.com/messages?username="+username;
        String text = "Some text";
        MessageList messages = new MessageList(Collections.singletonList(new Message(username, text)));
        ResponseEntity<MessageList> userMessages = ResponseEntity.ok(messages);
        Mockito.when(restTemplate.getForEntity(messageServiceUri, MessageList.class)).thenReturn(userMessages);

        // send actual request to get user's messages
        String uri = "http://localhost:" + port + "/users/" + user.getId() + "/messages";
        ResponseEntity<MessageList> responseEntity = testRestTemplate.getForEntity(uri, MessageList.class);

        // Should receive OK http status code
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Should receive only one message
        MessageList responseMessages = responseEntity.getBody();
        assertEquals(messages.getCount(), responseMessages.getCount());

        // Message username should be the same
        Message message = responseMessages.getMessages().get(0);
        assertEquals(username, message.getUsername());
        assertEquals(text, message.getText());
    }
}
