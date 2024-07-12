package com.khiemtran.security.rest;

import com.khiemtran.security.config.YamlConfig;
import com.khiemtran.security.domains.AccessTokenGoogleInfo;
import com.khiemtran.security.domains.RefreshTokenGoogleInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class GoogleRestAPI {
  private final YamlConfig yamlConfig;
  private final WebClient webClient;

  public AccessTokenGoogleInfo getAccessToken(String code) {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.put("code", Collections.singletonList(code));
    map.put("client_id", Collections.singletonList(yamlConfig.getClientId()));
    map.put("client_secret", Collections.singletonList(yamlConfig.getClientSecret()));
    map.put("redirect_uri", Collections.singletonList("http://localhost:8080/login/oauth2/code/google"));
    map.put("grant_type", Collections.singletonList("authorization_code"));
    return webClient.post()
        .uri(yamlConfig.getTokenUri())
        .body(BodyInserters.fromFormData(map))
        .retrieve()
        .bodyToMono(AccessTokenGoogleInfo.class)
        .block();
  }

  public RefreshTokenGoogleInfo refresh(String refreshToken) {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.put("client_id", Collections.singletonList(yamlConfig.getClientId()));
    map.put("client_secret", Collections.singletonList(yamlConfig.getClientSecret()));
    map.put("refresh_token", Collections.singletonList(refreshToken));
    map.put("grant_type", Collections.singletonList("refresh_token"));
    return webClient.post()
        .uri(yamlConfig.getTokenUri())
        .body(BodyInserters.fromFormData(map))
        .retrieve()
        .bodyToMono(RefreshTokenGoogleInfo.class)
        .block();
  }
}
