package com.example.smartpark.exception;

import org.springframework.http.HttpStatus;

/*
 *   SMARTPARK EXCEPTION
 */
public class SmartParkException extends RuntimeException{

    private final HttpStatus status;

    public SmartParkException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
