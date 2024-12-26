package com.khiemtran.controller;

import com.khiemtran.dto.request.LoginRequest;
import com.khiemtran.dto.request.SignUpRequest;
import com.khiemtran.dto.response.AccessTokenResponse;
import com.khiemtran.dto.response.UserResponse;
import com.khiemtran.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
@Tag(name = "Registration & Authentication & Refresh & Logout")
public class AuthController {
  private final static String URI_LOCATION = "/auth/{username}";
  private final AuthenticationService authenticationService;

  @PostMapping(value = "/sign-up", consumes = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Sign Up", description = "Create new user", responses = {
      @ApiResponse(responseCode = "201", description = "User is created successfully",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "400", description = "Invalid input",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string")))
  })
  public ResponseEntity<String> signup(@Valid @RequestBody SignUpRequest request) {
    UserResponse userResponse = authenticationService.register(request.sanitize(request));
    URI location = ServletUriComponentsBuilder
        .fromCurrentContextPath().path(URI_LOCATION)
        .buildAndExpand(userResponse.username()).toUri();
    return ResponseEntity.created(location).body("User is created successfully.");
  }

  @PostMapping(value = "/login")
  @Operation(summary = "Login in", description = "Authentication", responses = {
      @ApiResponse(responseCode = "200", description = "accessToken & refreshToken & expireTime",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(description = "Access token string",
                  example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", type = "object"))),
      @ApiResponse(responseCode = "400", description = "Invalid input",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "401", description = "Unauthorized",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string")))
  })
  public ResponseEntity<AccessTokenResponse> login(@RequestHeader String email, @RequestHeader String password) {
    LoginRequest loginRequest = new LoginRequest(email, password);
    return new ResponseEntity<>(authenticationService.authenticate(loginRequest.sanitize(loginRequest)), HttpStatus.OK);
  }

  @PostMapping(value = "/refresh")
  @Operation(summary = "refresh", description = "Refresh token", responses = {
      @ApiResponse(responseCode = "200", description = "accessToken & refreshToken & expireTime",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(description = "Access token string",
                  example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", type = "object"))),
      @ApiResponse(responseCode = "400", description = "Invalid input",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "401", description = "Unauthorized",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string")))
  })
  public ResponseEntity<AccessTokenResponse> refresh(@RequestHeader("refresh_token") String refreshToken,
                                                     HttpServletRequest request) {
    return new ResponseEntity<>(authenticationService.refresh(request), HttpStatus.OK);
  }

  @PostMapping(value = "/logout")
  @Operation(summary = "logout", description = "Logout", responses = {
      @ApiResponse(responseCode = "200", description = "User has successfully signed out.",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(description = "User has successfully signed out.",
                  example = "User has successfully signed out....", type = "string"))),
      @ApiResponse(responseCode = "400", description = "Invalid input",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "401", description = "Unauthorized",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string")))
  })
  public ResponseEntity<String> logout(@RequestHeader("access_token") String accessToken) {
    authenticationService.logout(accessToken);
    return new ResponseEntity<>("User has successfully signed out.", HttpStatus.OK);
  }
}