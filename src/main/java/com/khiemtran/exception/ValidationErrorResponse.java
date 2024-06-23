package com.khiemtran.exception;

import lombok.Data;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ValidationErrorResponse {
  List<FieldError> fieldErrors;

  public ValidationErrorResponse(List<ObjectError> allErrors) {
    this.fieldErrors = allErrors.stream()
        .map(objectError -> new FieldError(objectError.getDefaultMessage()))
        .collect(Collectors.toList());
  }
}

@Data
class FieldError {
  private String message;

  public FieldError(String defaultMessage) {
    this.message = defaultMessage;
  }
}

