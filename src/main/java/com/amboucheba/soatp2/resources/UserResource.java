package com.amboucheba.soatp2.resources;


import com.amboucheba.soatp2.exceptions.ApiException;
import com.amboucheba.soatp2.exceptions.NotFoundException;
import com.amboucheba.soatp2.exceptions.RemoteException;
import com.amboucheba.soatp2.models.MessageList;
import com.amboucheba.soatp2.models.User;
import com.amboucheba.soatp2.models.UserList;
import com.amboucheba.soatp2.repositories.UserRepository;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/users")
public class UserResource {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RestTemplate restTemplate ;

    @GetMapping(produces = "application/json")
    public ResponseEntity<UserList> getAll(){

        List<User> users = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new UserList(users));
    }

    @GetMapping(value = "/{userId}", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User returned in response body"),
            @ApiResponse(code = 404, message = "User not found")
    })
    public ResponseEntity<User> getUserById(@PathVariable("userId") long userId){

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()){
            throw new NotFoundException("User with id " + userId + " not found");
        }
        return ResponseEntity.ok(user.get());
    }

    @GetMapping(value = "/{userId}/messages", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User messages returned in response body"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 503, message = "Message service did not respond")
    })
    public ResponseEntity<MessageList> getUserMessagesById(@PathVariable("userId") long userId)  {

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()){
            throw new NotFoundException("User with id " + userId + " not found");
        }

        String username = user.get().getUsername();

        String uri = "https://amboucheba-soa-tp2.herokuapp.com/messages?username="+username;

        ResponseEntity<MessageList> responseEntity;
        try{
            responseEntity = restTemplate.getForEntity(uri, MessageList.class);
        }
        catch (RestClientException ce){
            throw new RemoteException("No response from Message service!");
        }

        MessageList messageList = responseEntity.getBody();
        return ResponseEntity.ok(messageList);

    }

    @PostMapping(consumes = "application/json" )
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User created, check location header for uri"),
            @ApiResponse(code = 400, message = "Provided User info not valid, check response body for more details on error")
    })
    public ResponseEntity<Void> addUser(@Valid @RequestBody User newUser){
        // NOT ENOUGH SPACE EXCEPTION
        /// ...
        User savedUser = userRepository.save(newUser);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/{userId}", consumes = "application/json", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User updated and returned in response body"),
            @ApiResponse(code = 201, message = "User created,  check location header for uri"),
            @ApiResponse(code = 400, message = "Provided User info not valid, check response body for more details on error")
    })
    public ResponseEntity<User> updateUser(@PathVariable("userId") long userId, @Valid @RequestBody User newUser){
        // NOT ENOUGH SPACE EXCEPTION
        /// ...

        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()){
            // user exists so just update it
            User actualUser = user.get();
            actualUser.setEmail(newUser.getEmail());
            actualUser.setUsername(newUser.getUsername());
            User savedUser = userRepository.save(actualUser);
            return ResponseEntity.ok(savedUser);
        }

        // user does not exist so create it
        User savedUser = userRepository.save(newUser);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("users")
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
