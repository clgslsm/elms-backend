package com.example.elms.exception;

public class EmailAreadyExisted extends RuntimeException {
    public EmailAreadyExisted(String message) {
        super(message);
    }
}
