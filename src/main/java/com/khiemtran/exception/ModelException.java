package com.khiemtran.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ModelException extends RuntimeException {
  public ModelException(String message) {
    super(message);
  }

  public ModelException(String message, Throwable cause) {
    super(message, cause);
  }

  public ModelException(ValidationErrorResponse validationErrorResponse) {
  }
}

