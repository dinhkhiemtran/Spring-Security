package com.khiemtran.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/greeting")
public class GreetingController {
  private static final String WELCOME = "Welcome to my tutorials.";

  @GetMapping(path = "")
  public ResponseEntity<String> greeting() {
    return ResponseEntity.ok(WELCOME);
  }

  @GetMapping(path = "/{name}")
  public ResponseEntity<String> getGreeting(@PathVariable("name") String name) {
    return ResponseEntity.ok("Hello " +
        name.substring(0, 1).toUpperCase() + name.substring(1) + "! " + WELCOME);
  }
}
