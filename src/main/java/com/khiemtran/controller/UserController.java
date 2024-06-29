package com.khiemtran.controller;

import com.khiemtran.dto.request.UserRequest;
import com.khiemtran.dto.response.UserResponse;
import com.khiemtran.service.UserService;
import com.khiemtran.utils.SanitizerUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {
  private final UserService userService;

  @GetMapping(path = "/user")
  @Operation(
      summary = "Login in",
      description = "Authentication",
      responses = {
          @ApiResponse(responseCode = "200",
              description = "Get Users",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(description = "List Users"))),
          @ApiResponse(responseCode = "400",
              description = "Invalid input",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string"))),
          @ApiResponse(responseCode = "401",
              description = "Unauthorized",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string")))
      })
  public ResponseEntity<List<UserResponse>> getAllUsers() {
    List<UserResponse> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }

  @PutMapping(path = "/user")
  @Operation(
      summary = "Login in",
      description = "Authentication",
      responses = {
          @ApiResponse(responseCode = "200",
              description = "Access token & Expire time",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(description = "Update User",
                      example = """
                              {
                              "username": "example",
                              "password": "example",
                              "email" : "example@mail.com",
                              "zipCode": "1234",
                              "city": "example"
                          }"""))),
          @ApiResponse(responseCode = "400",
              description = "Invalid input",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string"))),
          @ApiResponse(responseCode = "401",
              description = "Unauthorized",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string")))
      })
  public ResponseEntity<String> updateUser(@RequestParam("email") String email,
                                           @RequestBody @Valid UserRequest userRequest) {
    UserRequest userRequestSanitized = userRequest.sanitize(userRequest);
    userService.updateUser(SanitizerUtils.sanitizeString(email), userRequestSanitized);
    return ResponseEntity.ok("User is updated successfully.");
  }

  @DeleteMapping(path = "/user/{email}")
  @Operation(
      summary = "Login in",
      description = "Authentication",
      responses = {
          @ApiResponse(responseCode = "200",
              description = "Remove User",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(type = "string"))),
          @ApiResponse(responseCode = "400",
              description = "Invalid input",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string"))),
          @ApiResponse(responseCode = "401",
              description = "Unauthorized",
              content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string")))
      })
  public ResponseEntity<String> deleteUser(@PathVariable("email") String email) {
    userService.remove(SanitizerUtils.sanitizeString(email));
    return ResponseEntity.ok("User is removed successfully.");
  }
}
