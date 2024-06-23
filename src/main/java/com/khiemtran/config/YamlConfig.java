package com.khiemtran.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.application")
@Component
@Data
public class YamlConfig {
  private String jwtSecret;
  private String[] pathAllowed;
}
