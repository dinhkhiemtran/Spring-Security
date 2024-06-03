package com.khiemtran.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class GreetingController {
  @GetMapping("/")
  public ResponseEntity<String> greeting() {
    return ResponseEntity.ok("Hello World");
  }

  @GetMapping("/{name}")
  public ResponseEntity<String> getGreeting(@PathVariable("name") String name) {
    return ResponseEntity.ok("Hello " + name);
  }
}
