package com.khiemtran.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {
  @ExceptionHandler(UserAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.BAD_GATEWAY)
  public ModelException userAlreadyExistsException(UserAlreadyExistsException userAlreadyExistsException) {
    return new ModelException(userAlreadyExistsException.getMessage());
  }
}