package com.khiemtran.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greeting")
@Tag(name = "Greeting")
public class GreetingController {
  private static final String WELCOME = "Welcome to Spring Security.";

  @GetMapping
  @Operation(summary = "Greeting", description = "A greeting", responses = {
      @ApiResponse(responseCode = "200", description = "Greeting",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string")))})
  public ResponseEntity<String> greeting() {
    return ResponseEntity.ok("Hello you, " + WELCOME);
  }

  @GetMapping("/{name}")
  @Operation(summary = "Greeting", description = "A greeting", responses = {
      @ApiResponse(responseCode = "200", description = "Greeting",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "string")))})
  public ResponseEntity<String> getGreeting(@PathVariable String name) {
    String capitalized = name.substring(0, 1).toUpperCase() + name.substring(1);
    return ResponseEntity.ok("Hello " + capitalized + ", " + WELCOME);
  }
}