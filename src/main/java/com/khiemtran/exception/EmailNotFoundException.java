package com.khiemtran.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

@Getter
public class EmailNotFoundException extends InternalAuthenticationServiceException {
  private HttpStatus httpStatus;

  public EmailNotFoundException(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }

  public EmailNotFoundException(String message) {
    super(message);
  }
}
