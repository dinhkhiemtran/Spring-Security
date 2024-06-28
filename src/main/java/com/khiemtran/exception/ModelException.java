package com.khiemtran.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@RequiredArgsConstructor
public class ModelException {
  private String message;
  private HttpStatus httpStatus;

  public ModelException(String message) {
    this.message = message;
  }

  public ModelException(String message, HttpStatus httpStatus) {
    this.message = message;
    this.httpStatus = httpStatus;
  }
}

