package com.khiemtran.service;

import com.khiemtran.constants.TokenType;

import javax.crypto.SecretKey;

public interface SecretKeyService {
  SecretKey getKey(TokenType type);
}
