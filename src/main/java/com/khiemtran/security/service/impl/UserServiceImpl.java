package com.khiemtran.security.service.impl;

import com.khiemtran.security.dto.request.UserRequest;
import com.khiemtran.security.dto.response.UserResponse;
import com.khiemtran.security.exception.EmailNotFoundException;
import com.khiemtran.security.model.User;
import com.khiemtran.security.repository.UserRepository;
import com.khiemtran.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Override
  @Transactional
  public void save(UserRequest request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new IllegalArgumentException("User is existed.");
    }
    userRepository.save(new User(
        request.userName(),
        request.password(),
        request.email(),
        request.zipCode(),
        request.city()));
  }

  @Override
  @Transactional
  public List<UserResponse> getAllUsers() {
    List<User> users = userRepository.findAll();
    return users.stream()
        .map(user -> new UserResponse(
            user.getUserName(),
            user.getEmail(),
            user.getZipCode(),
            user.getCity()))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void updateUser(String email, UserRequest userRequest) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new EmailNotFoundException("User not found with email: " + email));
    user.setUserName(Optional.ofNullable(userRequest.userName())
        .orElse(user.getUserName()));
    user.setPassword(Optional.ofNullable(userRequest.password())
        .orElse(user.getPassword()));
    user.setEmail(Optional.ofNullable(userRequest.email())
        .orElse(user.getEmail()));
    user.setZipCode(Optional.ofNullable(userRequest.zipCode())
        .orElse(user.getZipCode()));
    user.setCity(Optional.ofNullable(userRequest.city())
        .orElse(user.getCity()));
    userRepository.save(user);
  }

  @Override
  @Transactional
  public void remove(String email) {
    userRepository.delete(userRepository.findByEmail(email)
        .orElseThrow(() -> new EmailNotFoundException("User not found with email: " + email)));
  }
}
