package com.khiemtran.service;

import com.khiemtran.dto.request.UserRequest;
import com.khiemtran.dto.response.UserResponse;

import java.util.List;

public interface UserService {
  List<UserResponse> getAllUsers();

  void updateUser(String email, UserRequest userRequest);

  void remove(String email);
}
