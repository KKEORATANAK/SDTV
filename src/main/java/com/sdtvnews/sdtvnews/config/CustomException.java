package com.sdtvnews.sdtvnews.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // or any other appropriate status
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
