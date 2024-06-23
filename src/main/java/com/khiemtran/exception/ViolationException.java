package com.khiemtran.exception;

import lombok.Data;

@Data
public class ViolationException {
  private String message;

  public ViolationException(String message) {
    this.message = message;
  }
}
