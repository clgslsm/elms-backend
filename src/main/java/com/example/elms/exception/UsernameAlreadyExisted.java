package com.example.elms.exception;

public class UsernameAlreadyExisted extends RuntimeException {
    public UsernameAlreadyExisted(String message) {
        super(message);
    }
}
