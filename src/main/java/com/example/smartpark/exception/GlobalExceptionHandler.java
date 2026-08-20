package com.example.smartpark.exception;

import com.example.smartpark.dto.ErrorResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
 *   GLOBAL EXCEPTION HANDLER
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SmartParkException.class)
    public ResponseEntity<ErrorResponseDTO> handleSmartParkException(SmartParkException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                ex.getStatus().value(),
                ex.getStatus().getReasonPhrase(),
                ex.getMessage()
        );

        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }
}
