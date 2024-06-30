package com.khiemtran.service;

import com.khiemtran.dto.request.LoginRequest;
import com.khiemtran.dto.request.SignUpRequest;
import com.khiemtran.dto.request.UserRequest;
import com.khiemtran.dto.response.AccessToken;
import com.khiemtran.dto.response.UserResponse;

import java.util.List;

public interface UserService {
  UserResponse create(SignUpRequest request);

  List<UserResponse> getAllUsers();

  void updateUser(String email, UserRequest userRequest);

  void remove(String email);

  AccessToken getAccessToken(LoginRequest loginRequest);
}
