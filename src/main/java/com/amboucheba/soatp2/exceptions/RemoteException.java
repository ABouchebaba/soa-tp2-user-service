package com.amboucheba.soatp2.exceptions;

public class RemoteException extends RuntimeException{

    public RemoteException(String message) {
        super(message);
    }

    public RemoteException(String message, Throwable cause) {
        super(message, cause);
    }
}
