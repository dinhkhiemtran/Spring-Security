package com.khiemtran.service.impl;

import com.khiemtran.dto.request.UserRequest;
import com.khiemtran.dto.response.UserResponse;
import com.khiemtran.exception.EmailNotFoundException;
import com.khiemtran.model.User;
import com.khiemtran.repository.RoleRepository;
import com.khiemtran.repository.UserRepository;
import com.khiemtran.security.provider.JwtProvider;
import com.khiemtran.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;
  private final JwtProvider jwtProvider;
  private final AuthenticationManager authenticationManager;

  @Override
  public List<UserResponse> getAllUsers() {
    List<User> users = userRepository.findAll();
    return users.stream()
        .map(user -> new UserResponse(
            user.getUsername(),
            user.getEmail(),
            user.getZipCode(),
            user.getCity()))
        .collect(Collectors.toList());
  }

  @Override
  public void updateUser(String email, UserRequest userRequest) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new EmailNotFoundException("User not found with email: " + email));
    user.setUsername(Optional.ofNullable(userRequest.username())
        .orElse(user.getUsername()));
    user.setPassword(
        Optional.ofNullable(passwordEncoder.encode(userRequest.password()))
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
  public void remove(String email) {
    userRepository.delete(userRepository.findByEmail(email)
        .orElseThrow(() -> new EmailNotFoundException("User not found with email: " + email)));
  }
}
