package com.khiemtran.controller;

import com.khiemtran.dto.request.SignUpRequest;
import com.khiemtran.dto.response.AccessToken;
import com.khiemtran.dto.response.UserResponse;
import com.khiemtran.service.UserService;
import com.khiemtran.utils.SanitizerUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
@Tag(name = "SignUp & Login")
public class AuthController {
  private final static String URI_LOCATION = "http://localhost:8080/api/v1/";
  private final UserService userService;

  @PostMapping(path = "/sign-up", consumes = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Sign Up",
      description = "Create new user",
      responses = {
          @ApiResponse(responseCode = "201",
              description = "User is created successfully",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string"))),
          @ApiResponse(responseCode = "400",
              description = "Invalid input",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string")))
      })
  public ResponseEntity<String> signup(@Valid @RequestBody SignUpRequest request) {
    SignUpRequest sanitized = request.sanitize(request);
    UserResponse userResponse = userService.create(sanitized);
    URI uri = URI.create(URI_LOCATION + userResponse.username());
    return ResponseEntity.created(uri).body("User is created successfully");
  }

  @PostMapping(path = "/login", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
      mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE
  ))
  @Operation(
      summary = "Login in",
      description = "Authentication",
      responses = {
          @ApiResponse(responseCode = "200",
              description = "Access token & Expire time",
              content = @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(description = "Access token string",
                      example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                      type = "object"))),
          @ApiResponse(responseCode = "400",
              description = "Invalid input",
              content = @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(type = "string"))),
          @ApiResponse(responseCode = "401",
              description = "Unauthorized",
              content = @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(type = "string")))
      })
  public ResponseEntity<AccessToken> login(
      @Parameter(name = "email", description = "email", example = "example@mail.com", required = true, in = ParameterIn.PATH)
      @RequestParam String email,
      @Parameter(name = "password", description = "password", example = "Abc1@3", required = true, in = ParameterIn.PATH)
      @RequestParam String password) {
    String emailSanitized = SanitizerUtils.sanitizeString(email);
    String passwordSanitized = SanitizerUtils.sanitizeString(password);
    AccessToken accessToken = userService.getAccessToken(emailSanitized, passwordSanitized);
    return ResponseEntity.ok(accessToken);
  }
}
