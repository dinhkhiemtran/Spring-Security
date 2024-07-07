package com.khiemtran.security.rest;

import com.khiemtran.security.config.YamlConfig;
import com.khiemtran.security.domains.AccessTokenGoogleInfo;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

@Component
public class GoogleRestAPI {
  private final YamlConfig yamlConfig;
  private final WebClient webClient;

  GoogleRestAPI(YamlConfig yamlConfig) {
    this.yamlConfig = yamlConfig;
    this.webClient = WebClient.builder().build();
  }

  public AccessTokenGoogleInfo accessTokenGoogleInfo(String code) {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.put("code", Collections.singletonList(code));
    map.put("client_id", Collections.singletonList(yamlConfig.getClientId()));
    map.put("client_secret", Collections.singletonList(yamlConfig.getClientSecret()));
    map.put("redirect_uri", Collections.singletonList("http://localhost:8080/oauth2/code/"));
    map.put("grant_type", Collections.singletonList("authorization_code"));
    return webClient.post()
        .uri("https://oauth2.googleapis.com/token")
        .body(BodyInserters.fromFormData(map))
        .retrieve()
        .bodyToMono(AccessTokenGoogleInfo.class)
        .block();
  }
}
