package com.amboucheba.soatp2;

import com.amboucheba.soatp2.models.MessageList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Client {

    public static void main(String[] args){

        String uri = "";

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<MessageList> response = restTemplate.getForEntity(uri, MessageList.class);

        System.out.println(response.getBody().toString());


    }
}
