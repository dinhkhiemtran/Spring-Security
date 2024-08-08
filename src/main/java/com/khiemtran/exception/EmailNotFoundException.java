package com.khiemtran.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EmailNotFoundException extends RuntimeException {
  private HttpStatus httpStatus;

  public EmailNotFoundException(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }

  public EmailNotFoundException(String message) {
    super(message);
  }
}
