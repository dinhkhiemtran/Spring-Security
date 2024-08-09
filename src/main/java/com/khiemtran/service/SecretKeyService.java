package com.khiemtran.service;

import com.khiemtran.config.YamlConfig;
import com.khiemtran.constants.TokenType;

import javax.crypto.SecretKey;

public interface SecretKeyService {
  SecretKey getKey(YamlConfig yamlConfig, TokenType type);
}
