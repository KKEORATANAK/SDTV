package com.sdtvnews.sdtvnews.config.exception;

import java.time.LocalDateTime;

public class ResponseDTO<T> {
    private String status; // e.g., "success" or "error"
    private LocalDateTime timestamp; // Current timestamp
    private String message; // Custom message
    private T data;

    public ResponseDTO(String status, String message, T data) {
        this.status = status;
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
