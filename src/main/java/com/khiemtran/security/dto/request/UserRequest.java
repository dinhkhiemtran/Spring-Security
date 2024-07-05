package com.khiemtran.security.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
    @NotBlank String username,
    @NotBlank @Email String email,
    @NotBlank String password,
    @NotBlank String zipCode,
    @NotBlank String city
) {
}
