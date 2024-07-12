package com.khiemtran.security.service;

import com.khiemtran.security.domains.AccessTokenGoogleInfo;
import com.khiemtran.security.domains.RefreshTokenGoogleInfo;

public interface GoogleService {
  AccessTokenGoogleInfo getAccessToken(String code);

  RefreshTokenGoogleInfo refresh();
}
