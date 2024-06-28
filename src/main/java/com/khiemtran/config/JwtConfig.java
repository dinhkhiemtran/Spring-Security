package com.khiemtran.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.application.jwt")
@Data
public class JwtConfig {
  private String jwtSecret;
  private long expireTime;
}
