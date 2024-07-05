package com.khiemtran.security.service;

import com.khiemtran.security.dto.request.UserRequest;

public interface UserService {
  String create(UserRequest userRequest);
}
