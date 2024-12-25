package com.khiemtran.exception;

import com.khiemtran.model.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

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

