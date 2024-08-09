package com.khiemtran.service;

import com.khiemtran.constants.TokenType;
import com.khiemtran.utils.UserPrincipal;

public interface JwtService {
  String generateToken(UserPrincipal userPrincipal, TokenType tokenType, long expireTime);

  String generateRefreshToken(UserPrincipal userPrincipal, TokenType tokenType, long expireDay);

  String extractToken(String token, TokenType type);

  boolean isValidationToken(String token, TokenType tokenType, UserPrincipal userPrincipal);
}
