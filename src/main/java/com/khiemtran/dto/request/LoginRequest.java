package com.khiemtran.dto.request;

import com.khiemtran.security.Sanitizer;
import com.khiemtran.utils.SanitizerUtils;

import java.io.Serializable;

public record LoginRequest(String email, String password) implements Sanitizer<LoginRequest>, Serializable {
  @Override
  public LoginRequest sanitize(LoginRequest loginRequest) {
    return new LoginRequest(SanitizerUtils.sanitizeString(loginRequest.email), SanitizerUtils.sanitizeString(password));
  }
}
