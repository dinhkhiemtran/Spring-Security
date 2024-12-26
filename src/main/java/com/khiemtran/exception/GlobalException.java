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

  @ExceptionHandler({
      IllegalArgumentException.class,
      EmailNotFoundException.class,
      RoleNotFoundException.class,
      ResourceNotFoundException.class
  })
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ModelException handleUnauthorizedExceptions(RuntimeException e) {
    log.error(e.getMessage(), e);
    return new ModelException(e.getMessage(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ValidationErrorResponse handleValidationExceptions(MethodArgumentNotValidException e) {
    log.error(e.getMessage(), e);
    return new ValidationErrorResponse(e.getAllErrors());
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ModelException handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
    log.error(e.getMessage(), e);
    return new ModelException(e.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ModelException handleNoResourceFound(NoResourceFoundException e) {
    log.error(e.getMessage(), e);
    return new ModelException(e.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public List<ViolationException> handleConstraintViolations(ConstraintViolationException e) {
    log.error(e.getMessage(), e);
    return e.getConstraintViolations().stream()
        .map(violation -> new ViolationException(violation.getMessage()))
        .toList();
  }

  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ModelException handleRuntimeExceptions(RuntimeException e) {
    log.error(e.getMessage(), e);
    return new ModelException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}