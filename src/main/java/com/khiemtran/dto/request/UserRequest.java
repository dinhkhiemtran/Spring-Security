package com.khiemtran.dto.request;

import com.khiemtran.security.Sanitizer;
import com.khiemtran.utils.SanitizerUtils;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record UserRequest(
    @NotBlank(message = "Username is required") String username,
    @NotBlank String password,
    @NotBlank(message = "Email is required") @Email String email,
    @NotBlank String zipCode,
    String city) implements Sanitizer<UserRequest>, Serializable {
  @Override
  public UserRequest sanitize(UserRequest request) {
    return new UserRequest(
        SanitizerUtils.sanitizeString(request.username),
        SanitizerUtils.sanitizeString(request.password),
        SanitizerUtils.sanitizeString(request.email),
        SanitizerUtils.sanitizeString(request.zipCode),
        SanitizerUtils.sanitizeString(request.city)
    );
  }
}
