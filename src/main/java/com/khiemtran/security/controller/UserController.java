package com.khiemtran.security.controller;

import com.khiemtran.security.dto.request.UserRequest;
import com.khiemtran.security.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping(value = "/user")
  public ResponseEntity<String> create(@RequestBody @Valid UserRequest userRequest) {
    String message = userService.create(userRequest);
    return ResponseEntity.ok(message);
  }
}
