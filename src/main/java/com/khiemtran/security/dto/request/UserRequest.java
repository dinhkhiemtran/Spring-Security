package com.khiemtran.security.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
    @NotBlank(message = "Username is required") String userName,
    @NotBlank @Size(max = 50, message = "The password must be 50 characters or fewer.") String password,
    @NotBlank(message = "Email is required") @Email String email,
    @NotBlank @Size(max = 4, message = "The zip code must be 4 digits.") String zipCode,
    String city
) {
}
