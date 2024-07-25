package com.khiemtran.controller;

import com.khiemtran.dto.request.LoginRequest;
import com.khiemtran.dto.request.SignUpRequest;
import com.khiemtran.dto.response.AccessToken;
import com.khiemtran.dto.response.UserResponse;
import com.khiemtran.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
@Tag(name = "SignUp & Login")
public class AuthController {
  private final static String URI_LOCATION = "/api/users/{username}";
  private final UserService userService;

  @PostMapping(value = "/sign-up", consumes = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Sign Up", description = "Create new user", responses = {
      @ApiResponse(responseCode = "201", description = "User is created successfully",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "400",
          description = "Invalid input",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string")))
  })
  public ResponseEntity<String> signup(@Valid @RequestBody SignUpRequest request) {
    SignUpRequest sanitized = request.sanitize(request);
    UserResponse userResponse = userService.create(sanitized);
    URI location = ServletUriComponentsBuilder
        .fromCurrentContextPath().path(URI_LOCATION)
        .buildAndExpand(userResponse.username()).toUri();
    return ResponseEntity.created(location).body("User is created successfully");
  }

  @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
      mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE
  ))
  @Operation(summary = "Login in", description = "Authentication", responses = {
      @ApiResponse(responseCode = "200", description = "Access token & Expire time",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(description = "Access token string",
                  example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", type = "object"))),
      @ApiResponse(responseCode = "400", description = "Invalid input",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "401", description = "Unauthorized",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string")))
  })
  public ResponseEntity<AccessToken> login(@RequestParam String email, @Schema(type = "string", format = "password") @RequestParam String password) {
    LoginRequest loginRequest = new LoginRequest(email, password);
    LoginRequest sanitized = loginRequest.sanitize(loginRequest);
    AccessToken accessToken = userService.getAccessToken(sanitized);
    return new ResponseEntity<>(accessToken, HttpStatus.OK);
  }
}