package com.khiemtran.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.application")
@Component
@Data
public class YamlConfig {
  private String name;
  private String[] whiteList;
  private Jwt jwt;

  @Data
  public static class Jwt {
    private String jwtSecret;
    private long expireTime;
  }
}
