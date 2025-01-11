package com.task.manager.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiResponse<T> {

    private String status;
    private String message;
    private T data;
    private String timestamp;

    public ApiResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        timestamp =  LocalDateTime.now().toString();
    }

}
