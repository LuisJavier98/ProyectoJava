package com.proyecto.Proyecto.Exceptions;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HashMap<String,Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<HashMap<String,Object>> handleHttpMessageFileSizeLimitExceededException(FileSizeLimitExceededException ex) {
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<HashMap<String,Object>> handleHttpMessageUsernameNotFoundException(UsernameNotFoundException ex) {
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HashMap<String,Object>> handleHttpMessageMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        HashMap<String, Object> response = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error-> response.put(((FieldError)error).getField(),error.getDefaultMessage()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}