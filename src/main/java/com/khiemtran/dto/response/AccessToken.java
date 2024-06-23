package com.khiemtran.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccessToken(
   @JsonProperty("accessToken") String accessToken,
   @JsonProperty("expireTime") Long expiryDate) {
}
