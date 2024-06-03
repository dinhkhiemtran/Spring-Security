package com.khiemtran.security.service;

import com.khiemtran.security.dto.request.UserRequest;
import com.khiemtran.security.dto.response.UserResponse;

import java.util.List;

public interface UserService {
  void save(UserRequest request);

  List<UserResponse> getAllUsers();

  void updateUser(String email, UserRequest userRequest);

  void remove(String email);
}
