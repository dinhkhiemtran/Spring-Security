package com.khiemtran.security.service.impl;

import com.khiemtran.security.dto.request.UserRequest;
import com.khiemtran.security.exception.UserAlreadyExistsException;
import com.khiemtran.security.model.User;
import com.khiemtran.security.repository.UserRepository;
import com.khiemtran.security.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public String create(UserRequest userRequest) {
    if (userRepository.existsByEmail(userRequest.email())) {
      throw new UserAlreadyExistsException("User already exist.");
    }
    User user = new User(userRequest);
    try {
      userRepository.save(user);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return "User created successfully.";
  }
}
