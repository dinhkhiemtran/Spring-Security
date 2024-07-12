package com.khiemtran.security.domains;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AccessTokenGoogleInfo {
  @JsonProperty("access_token")
  private String accessToken;
  @JsonProperty("expires_in")
  private long expiresIn;
  @JsonProperty("refresh_token")
  private String refreshToken;
  @JsonProperty("scope")
  private String scope;
  @JsonProperty("token_type")
  private String tokenType;
  @JsonProperty("id_token")
  private String idToken;
}
