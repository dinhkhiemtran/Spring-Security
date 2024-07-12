package com.khiemtran.security.service.impl;

import com.khiemtran.security.domains.AccessTokenGoogleInfo;
import com.khiemtran.security.domains.RefreshTokenGoogleInfo;
import com.khiemtran.security.rest.GoogleRestAPI;
import com.khiemtran.security.service.GoogleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleServiceImpl implements GoogleService {
  private final GoogleRestAPI googleRestAPI;
  private AccessTokenGoogleInfo accessTokenGoogleInfo = new AccessTokenGoogleInfo();

  @Override
  public AccessTokenGoogleInfo getAccessToken(String code) {
    accessTokenGoogleInfo = googleRestAPI.getAccessToken(code);
    return accessTokenGoogleInfo;
  }

  @Override
  public RefreshTokenGoogleInfo refresh() {
    String refreshToken = accessTokenGoogleInfo.getRefreshToken();
    if (refreshToken != null) {
      return googleRestAPI.refresh(refreshToken);
    } else {
      throw new IllegalArgumentException("Refresh token is invalid or does not exist");
    }
  }
}
