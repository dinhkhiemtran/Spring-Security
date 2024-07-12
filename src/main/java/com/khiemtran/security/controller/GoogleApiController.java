package com.khiemtran.security.controller;

import com.khiemtran.security.domains.AccessTokenGoogleInfo;
import com.khiemtran.security.domains.RefreshTokenGoogleInfo;
import com.khiemtran.security.service.GoogleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth2/google")
@RequiredArgsConstructor
public class GoogleApiController {
  private final GoogleService googleService;
  @PostMapping("/authentication")
  public ResponseEntity<AccessTokenGoogleInfo> getAccessToken(@RequestParam("code") String code) {
    AccessTokenGoogleInfo accessTokenGoogleInfo = googleService.getAccessToken(code);
    return ResponseEntity.ok(accessTokenGoogleInfo);
  }

  @PostMapping("/refresh")
  public ResponseEntity<RefreshTokenGoogleInfo> refresh() {
    RefreshTokenGoogleInfo refreshTokenGoogleInfo = googleService.refresh();
    return ResponseEntity.ok(refreshTokenGoogleInfo);
  }
}
