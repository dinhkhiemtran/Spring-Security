package com.khiemtran.service;

import com.khiemtran.dto.response.AccessToken;
import com.khiemtran.utils.UserPrincipal;
import org.springframework.security.core.Authentication;

public interface JwtService {
  AccessToken generateToken(Authentication authentication);

  String extractToken(String token);

  boolean isValidationToken(String token, UserPrincipal userPrincipal);
}
