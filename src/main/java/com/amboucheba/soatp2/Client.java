package com.amboucheba.soatp2;

import com.amboucheba.soatp2.models.MessageList;
import com.amboucheba.soatp2.models.UserList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

public class Client {

    public static void main(String[] args){

        System.out.println("List of users:");
        Optional<UserList> userList = getAllUsers();
        userList.ifPresent(list -> System.out.println(list.toString()));


        long id = 1L;
        System.out.println("List of messages for user with id = " + id);
        Optional<MessageList> messageList = getUserMessages(id);
        messageList.ifPresent(list -> System.out.println(list.toString()));
    }

    private static Optional<MessageList> getUserMessages(long userId){
        String uri = "https://amboucheba-soa-tp2-user.herokuapp.com/users/" + userId + "/messages";

        RestTemplate restTemplate = new RestTemplate();

        try{
            ResponseEntity<MessageList> response = restTemplate.getForEntity(uri, MessageList.class);
            MessageList messages = response.getBody();

            return Optional.of(messages) ;
        }
        catch (HttpClientErrorException ce){
            System.out.println("Client error : " + ce.getMessage());
        }
        catch (HttpServerErrorException se){
            System.out.println("Server error : " + se.getStatusText());
        }
        return Optional.empty();
    }

    private static Optional<UserList> getAllUsers(){
        String uri = "https://amboucheba-soa-tp2-user.herokuapp.com/users";

        RestTemplate restTemplate = new RestTemplate();

        try{
            ResponseEntity<UserList> response = restTemplate.getForEntity(uri, UserList.class);
            UserList userList = response.getBody();

            return Optional.of(userList);
        }
        catch (HttpClientErrorException ce){
            System.out.println("Client error : " + ce.getMessage());
        }
        catch (HttpServerErrorException se){
            System.out.println("Server error : " + se.getStatusText());
        }

        return Optional.empty();
    }
}
