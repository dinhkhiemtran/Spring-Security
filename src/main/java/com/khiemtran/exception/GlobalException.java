package com.khiemtran.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<String> illegalArgumentExceptionHandler(IllegalArgumentException exception) {
    return ResponseEntity.badRequest().body(exception.getMessage());
  }

  @ExceptionHandler(EmailNotFoundException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<String> emailNotFoundExceptionHandler(EmailNotFoundException emailNotFoundException) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(emailNotFoundException.getMessage());
  }

  @ExceptionHandler(RoleNotFoundException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<String> roleNotFoundExceptionHandler(RoleNotFoundException exception) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ValidationErrorResponse> methodArgumentNotValidExceptionHandler(
      MethodArgumentNotValidException methodArgumentNotValidException) {
    List<ObjectError> allErrors = methodArgumentNotValidException.getAllErrors();
    ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse(allErrors);
    return ResponseEntity.badRequest().body(validationErrorResponse);
  }

  @ExceptionHandler(JwKeyNotRegisteredException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public String jwKeyNotRegisteredExceptionHandler(JwKeyNotRegisteredException jwKeyNotRegisteredException) {
    return jwKeyNotRegisteredException.getMessage();
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public String usernameNotFoundException(UsernameNotFoundException usernameNotFoundException) {
    return usernameNotFoundException.getMessage();
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String httpRequestMethodNotSupportedExceptionHandler(
      HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException) {
    return httpRequestMethodNotSupportedException.getMessage();
  }

  @ExceptionHandler(NoResourceFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String noResourceFoundException(NoResourceFoundException noResourceFoundException) {
    return noResourceFoundException.getMessage();
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public List<ViolationException> constraintViolationExceptionHandler(ConstraintViolationException constraintViolationException) {
    return constraintViolationException.getConstraintViolations()
        .stream().map(e -> new ViolationException(e.getMessage()))
        .toList();
  }
}

