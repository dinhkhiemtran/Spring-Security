package com.khiemtran.dto.request;

import com.khiemtran.security.Sanitizer;
import com.khiemtran.utils.SanitizerUtils;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record SignUpRequest(
    @NotBlank(message = "username is required") String username,
    @NotBlank(message = "password is required") String password,
    @NotBlank(message = "email is required") @Email String email,
    @NotBlank(message = "zipCode is required") @Size(min = 5, max = 5, message = "size must be 5") String zipCode,
    String city) implements Sanitizer<SignUpRequest>, Serializable {
  @Override
  public SignUpRequest sanitize(SignUpRequest signUpRequest) {
    return new SignUpRequest(
        SanitizerUtils.sanitizeString(signUpRequest.username),
        SanitizerUtils.sanitizeString(signUpRequest.password),
        SanitizerUtils.sanitizeString(signUpRequest.email),
        SanitizerUtils.sanitizeString(signUpRequest.zipCode),
        SanitizerUtils.sanitizeString(signUpRequest.city)
    );
  }
}
