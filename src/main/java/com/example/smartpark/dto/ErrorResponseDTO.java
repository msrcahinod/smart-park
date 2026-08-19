package com.example.smartpark.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ErrorResponseDTO {

    private final int statusCode;

    private final String statusReason;

    private final String message;

    private final LocalDateTime timestamp;

    public ErrorResponseDTO(int statusCode, String statusReason, String message) {
        this.statusCode = statusCode;
        this.statusReason = statusReason;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

}
