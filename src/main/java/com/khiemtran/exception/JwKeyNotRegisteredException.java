package com.khiemtran.exception;

import lombok.Data;

@Data
public class JwKeyNotRegisteredException extends RuntimeException {
  private String message;

  public JwKeyNotRegisteredException(String message) {
    this.message = message;
  }
}
