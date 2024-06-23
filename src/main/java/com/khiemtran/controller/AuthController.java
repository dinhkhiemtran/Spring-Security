package com.khiemtran.controller;

import com.khiemtran.dto.request.SignUpRequest;
import com.khiemtran.dto.response.AccessToken;
import com.khiemtran.dto.response.UserResponse;
import com.khiemtran.service.UserService;
import com.khiemtran.utils.SanitizerUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class AuthController {
  private final static String URI_LOCATION = "http://localhost:8080/api/v1/";
  private final UserService userService;

  @PostMapping(path = "/sign-up", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> signup(@Valid @RequestBody SignUpRequest request) {
    SignUpRequest sanitized = request.sanitize(request);
    UserResponse userResponse = userService.create(sanitized);
    URI uri = URI.create(URI_LOCATION + userResponse.username());
    return ResponseEntity.created(uri).body("User is created successfully");
  }

  @PostMapping(path = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity<AccessToken> login(@RequestParam("email") String email,
                                           @RequestParam("password") String password) {
    String emailSanitized = SanitizerUtils.sanitizeString(email);
    String passwordSanitized = SanitizerUtils.sanitizeString(password);
    AccessToken accessToken = userService.getAccessToken(emailSanitized, passwordSanitized);
    return ResponseEntity.ok(accessToken);
  }
}
