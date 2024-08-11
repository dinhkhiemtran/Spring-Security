package com.khiemtran.service;

import com.khiemtran.dto.request.LoginRequest;
import com.khiemtran.dto.request.SignUpRequest;
import com.khiemtran.dto.response.AccessTokenResponse;
import com.khiemtran.dto.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
  UserResponse register(SignUpRequest request);

  AccessTokenResponse authenticate(LoginRequest loginRequest);

  AccessTokenResponse refresh(HttpServletRequest request);

  void logout(String accessToken);
}
