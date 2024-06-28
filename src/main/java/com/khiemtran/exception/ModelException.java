package com.khiemtran.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@RequiredArgsConstructor
public class ModelException {
  private String message;
  private Throwable throwable;
  private HttpStatus httpStatus;
  private ValidationErrorResponse validationErrorResponse;

  public ModelException(String message) {
    this.message = message;
  }

  public ModelException(String message, HttpStatus httpStatus) {
    this.message = message;
    this.httpStatus = httpStatus;
  }

  public ModelException(String message, Throwable cause) {
    this.message = message;
    this.throwable = cause;
  }

  public ModelException(String message, Throwable throwable, HttpStatus httpStatus) {
    this.message = message;
    this.throwable = throwable;
    this.httpStatus = httpStatus;
  }
}

