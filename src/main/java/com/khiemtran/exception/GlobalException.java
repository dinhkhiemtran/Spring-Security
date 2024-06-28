package com.khiemtran.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@RestControllerAdvice
public class GlobalException {
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ModelException illegalArgumentExceptionHandler(IllegalArgumentException exception) {
    return new ModelException(exception.getMessage(), exception.getCause(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(EmailNotFoundException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ModelException emailNotFoundExceptionHandler(EmailNotFoundException emailNotFoundException) {
    return new ModelException(emailNotFoundException.getMessage(), emailNotFoundException.getCause(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(RoleNotFoundException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ModelException roleNotFoundExceptionHandler(RoleNotFoundException exception) {
    return new ModelException(exception.getMessage(), exception.getCause(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ValidationErrorResponse methodArgumentNotValidExceptionHandler(
      MethodArgumentNotValidException methodArgumentNotValidException) {
    List<ObjectError> allErrors = methodArgumentNotValidException.getAllErrors();
    return new ValidationErrorResponse(allErrors);
  }

  @ExceptionHandler(JwKeyNotRegisteredException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ModelException jwKeyNotRegisteredExceptionHandler(JwKeyNotRegisteredException jwKeyNotRegisteredException) {
    return new ModelException(jwKeyNotRegisteredException.getMessage(),
        jwKeyNotRegisteredException.getCause(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ModelException usernameNotFoundException(UsernameNotFoundException usernameNotFoundException) {
    return new ModelException(usernameNotFoundException.getMessage(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ModelException httpRequestMethodNotSupportedExceptionHandler(
      HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException) {
    return new ModelException(httpRequestMethodNotSupportedException.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ModelException noResourceFoundException(NoResourceFoundException noResourceFoundException) {
    return new ModelException(noResourceFoundException.getMessage(),
        noResourceFoundException.getCause(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public List<ViolationException> constraintViolationExceptionHandler(ConstraintViolationException constraintViolationException) {
    return constraintViolationException.getConstraintViolations()
        .stream().map(e -> new ViolationException(e.getMessage()))
        .toList();
  }
}

