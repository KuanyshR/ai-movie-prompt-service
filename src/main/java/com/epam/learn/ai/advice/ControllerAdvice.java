package com.epam.learn.ai.advice;

import com.epam.learn.ai.exception.ModelNotFoundException;
import com.epam.learn.ai.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<?> exceptionHandler(Exception e){

        if(e instanceof ModelNotFoundException) {

            log.error(e.getMessage());

            var errorResponse = ErrorResponse.builder()
                    .code(400)
                    .message(e.getMessage())
                    .build();

            return ResponseEntity
                    .status(400)
                    .body(errorResponse);
        }
        return ResponseEntity
                .status(500)
                .body("Problem occured while chatting with AI");
    }
}
