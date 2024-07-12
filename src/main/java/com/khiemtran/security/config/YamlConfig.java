package com.khiemtran.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("spring.security.oauth2.client.registration.google")
@Data
@Component
public class YamlConfig {
  private String clientId;
  private String clientSecret;
  private String[] scope;
  private String tokenUri;
  private String userInfoUri;
  private String redirectUri;
  private String clientName;
}

