package com.khiemtran.security.controller;

import com.khiemtran.security.dto.request.UserRequest;
import com.khiemtran.security.dto.response.UserResponse;
import com.khiemtran.security.service.UserService;
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

  @PostMapping("/user")
  public ResponseEntity<String> saveUser(@RequestBody @Valid UserRequest request) {
    userService.save(request);
    return ResponseEntity.ok("User registered successfully.");
  }

  @GetMapping("/user")
  public ResponseEntity<List<UserResponse>> getAllUsers() {
    List<UserResponse> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }

  @PutMapping("/user")
  public ResponseEntity<String> updateUser(@RequestParam String email, @RequestBody @Valid UserRequest userRequest) {
    userService.updateUser(email, userRequest);
    return ResponseEntity.ok("User updated successfully.");
  }

  @DeleteMapping("/user/{email}")
  public ResponseEntity<String> deleteUser(@PathVariable("email") String email) {
    userService.remove(email);
    return ResponseEntity.ok("User removed successfully.");
  }
}
