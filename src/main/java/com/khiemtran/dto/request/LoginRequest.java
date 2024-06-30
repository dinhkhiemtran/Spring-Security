package com.khiemtran.dto.request;

import com.khiemtran.security.Sanitizer;
import com.khiemtran.utils.SanitizerUtils;

public record LoginRequest(String email, String password) implements Sanitizer<LoginRequest> {
  @Override
  public LoginRequest sanitize(LoginRequest loginRequest) {
    return new LoginRequest(SanitizerUtils.sanitizeString(loginRequest.email), SanitizerUtils.sanitizeString(password));
  }
}
