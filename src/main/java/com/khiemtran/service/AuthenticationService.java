package com.khiemtran.service;

import com.khiemtran.dto.request.LoginRequest;
import com.khiemtran.dto.request.SignUpRequest;
import com.khiemtran.dto.response.AccessToken;
import com.khiemtran.dto.response.UserResponse;

public interface AuthenticationService {
  UserResponse register(SignUpRequest request);

  AccessToken authenticate(LoginRequest loginRequest);
}
