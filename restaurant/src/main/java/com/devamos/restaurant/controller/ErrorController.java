package com.devamos.restaurant.controller;

import com.devamos.restaurant.domain.dtos.ErrorDto;
import com.devamos.restaurant.exceptions.BaseException;
import com.devamos.restaurant.exceptions.RestaurantNotFoundException;
import com.devamos.restaurant.exceptions.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@Slf4j
@RestController
@ControllerAdvice
public class ErrorController {
    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<ErrorDto> handleRestaurantNotFoundException(RestaurantNotFoundException ex) {
        log.error("Caught RestaurantNotFoundException", ex);

        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message("The specified restaurant wasn't found.")
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Caught MethodArgumentNotValidException", ex);
        String errorMsg = ex.getBindingResult()
                .getFieldErrors()
                .stream().map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(errorMsg)
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ErrorDto> handleStorageException(StorageException ex) {
        log.error("Caught StorageException", ex);

        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Unable to save or retrieve resource at this time.")
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handling base application exception
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorDto> handleBaseException(BaseException ex) {
        log.error("Caught BaseException", ex);

        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error occurred.")
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Catch all for unexpected exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        log.error("Caught unexpected exception.", ex);

        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error occurred.")
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
