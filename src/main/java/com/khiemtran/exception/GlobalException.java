package com.khiemtran.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalException {
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ModelException illegalArgumentExceptionHandler(IllegalArgumentException e) {
    log.error(e.getMessage(), e);
    return new ModelException(e.getMessage(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(EmailNotFoundException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ModelException emailNotFoundExceptionHandler(EmailNotFoundException e) {
    log.error(e.getMessage(), e);
    return new ModelException(e.getMessage(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(RoleNotFoundException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ModelException roleNotFoundExceptionHandler(RoleNotFoundException e) {
    log.error(e.getMessage(), e);
    return new ModelException(e.getMessage(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ValidationErrorResponse methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
    List<ObjectError> allErrors = e.getAllErrors();
    log.error(e.getMessage(), e);
    return new ValidationErrorResponse(allErrors);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ModelException usernameNotFoundException(ResourceNotFoundException e) {
    log.error(e.getMessage(), e);
    return new ModelException(e.getMessage(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ModelException httpRequestMethodNotSupportedExceptionHandler(
      HttpRequestMethodNotSupportedException e) {
    log.error(e.getMessage(), e);
    return new ModelException(e.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ModelException noResourceFoundException(NoResourceFoundException e) {
    log.error(e.getMessage(), e);
    return new ModelException(e.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public List<ViolationException> constraintViolationExceptionHandler(ConstraintViolationException e) {
    log.error(e.getMessage(), e);
    return e.getConstraintViolations()
        .stream().map(er -> new ViolationException(er.getMessage()))
        .toList();
  }

  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ModelException runtimeExceptionHandler(RuntimeException e) {
    log.error(e.getMessage(), e);
    return new ModelException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}

