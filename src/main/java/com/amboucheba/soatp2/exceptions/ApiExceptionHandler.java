package com.amboucheba.soatp2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;

@ControllerAdvice
public class ApiExceptionHandler {


    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e){
        ApiException apiException = new ApiException(e.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND );
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        ApiException apiException = new ApiException(message, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST );
    }

    @ExceptionHandler(value = {RemoteException.class})
    public ResponseEntity<Object> handleRemoteException(RemoteException e){
        ApiException apiException = new ApiException(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        return new ResponseEntity<>(apiException, HttpStatus.SERVICE_UNAVAILABLE );
    }

    // Unused: Replaced with MethodArgumentNotValidException
    @ExceptionHandler(value = {PayloadTooLargeException.class})
    public ResponseEntity<Object> handlePayloadTooLargeException(PayloadTooLargeException e){
        ApiException apiException = new ApiException(e.getMessage(), HttpStatus.PAYLOAD_TOO_LARGE);
        return new ResponseEntity<>(apiException, HttpStatus.PAYLOAD_TOO_LARGE );
    }
}
