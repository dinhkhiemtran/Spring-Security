package com.khiemtran.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalException {
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<String> illegalArgumentExceptionHandler(IllegalArgumentException exception) {
    return ResponseEntity.badRequest().body(exception.getMessage());
  }
  @ExceptionHandler(UsernameNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<String> emailNotFoundExceptionHandler(UsernameNotFoundException usernameNotFoundException) {
    return ResponseEntity.badRequest().body(usernameNotFoundException.getMessage());
  }
}
