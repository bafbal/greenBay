package com.bafbal.greenbay.exceptions;

import com.bafbal.greenbay.dtos.SimpleErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(GreenBayException.class)
  public ResponseEntity<String> handleGreenBayException(GreenBayException greenBayException) {
    return new ResponseEntity(new SimpleErrorDTO(greenBayException.getMessage()), HttpStatus.BAD_REQUEST);
  }
}
