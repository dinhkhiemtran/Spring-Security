package com.khiemtran.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccessTokenResponse(
    @JsonProperty("accessToken") String accessToken,
    @JsonProperty("refreshToken") String refreshToken,
    @JsonProperty("expireTime") Long expiryDate) {
}
