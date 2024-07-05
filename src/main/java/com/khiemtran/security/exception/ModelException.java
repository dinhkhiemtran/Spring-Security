package com.khiemtran.security.exception;

public class ModelException {
  private String message;

  public ModelException(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
