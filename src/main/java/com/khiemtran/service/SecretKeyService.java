package com.khiemtran.service;

import com.khiemtran.config.YamlConfig;

import javax.crypto.SecretKey;

public interface SecretKeyService {
  SecretKey getKey(YamlConfig yamlConfig);
}
