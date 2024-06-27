package com.khiemtran.controller;

import com.khiemtran.dto.request.UserRequest;
import com.khiemtran.dto.response.UserResponse;
import com.khiemtran.service.UserService;
import com.khiemtran.utils.SanitizerUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping(path = "/user")
  public ResponseEntity<List<UserResponse>> getAllUsers() {
    List<UserResponse> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }

  @PutMapping(path = "/user")
  public ResponseEntity<String> updateUser(@RequestParam("email") String email,
                                           @RequestBody @Valid UserRequest userRequest) {
    UserRequest userRequestSanitized = userRequest.sanitize(userRequest);
    userService.updateUser(SanitizerUtils.sanitizeString(email), userRequestSanitized);
    return ResponseEntity.ok("User is updated successfully.");
  }

  @DeleteMapping(path = "/user/{email}")
  public ResponseEntity<String> deleteUser(@PathVariable("email") String email) {
    userService.remove(SanitizerUtils.sanitizeString(email));
    return ResponseEntity.ok("User is removed successfully.");
  }
}
