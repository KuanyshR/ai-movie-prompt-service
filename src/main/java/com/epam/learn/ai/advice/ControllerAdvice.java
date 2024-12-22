package com.epam.learn.ai.advice;

import com.epam.learn.ai.exception.NotFoundException;
import com.epam.learn.ai.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(exception = NotFoundException.class)
    public ResponseEntity<?> exceptionHandler(Exception e){

        log.error(e.getMessage());

        var errorResponse = ErrorResponse.builder()
                .code(400)
                .message(e.getMessage())
                .build();

        return ResponseEntity
                .status(400)
                .body(errorResponse);

    }
}
