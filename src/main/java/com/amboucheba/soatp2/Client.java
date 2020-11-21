package com.amboucheba.soatp2;

import com.amboucheba.soatp2.models.MessageList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

public class Client {

    public static void main(String[] args){

        long id = 1L;

        String uri = "https://amboucheba-soa-tp2-user.herokuapp.com/users/" + id + "/messages";

        RestTemplate restTemplate = new RestTemplate();

        try{
            ResponseEntity<MessageList> response = restTemplate.getForEntity(uri, MessageList.class);
            MessageList messages = response.getBody();

            System.out.println(messages.toString());
        }
        catch (HttpClientErrorException ce){
            System.out.println("Client error : " + ce.getMessage());
        }
        catch (HttpServerErrorException se){
            System.out.println("Server error : " + se.getMessage());
        }

    }
}
