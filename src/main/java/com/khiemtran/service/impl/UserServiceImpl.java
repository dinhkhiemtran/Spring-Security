package com.khiemtran.service.impl;

import com.khiemtran.dto.request.SignUpRequest;
import com.khiemtran.dto.request.UserRequest;
import com.khiemtran.dto.response.AccessToken;
import com.khiemtran.dto.response.UserResponse;
import com.khiemtran.exception.EmailNotFoundException;
import com.khiemtran.exception.RoleNotFoundException;
import com.khiemtran.model.Role;
import com.khiemtran.model.User;
import com.khiemtran.repository.RoleRepository;
import com.khiemtran.repository.UserRepository;
import com.khiemtran.security.provider.JwtProvider;
import com.khiemtran.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.khiemtran.model.RoleName.ROLE_ADMIN;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;
  private final JwtProvider jwtProvider;
  private final AuthenticationManager authenticationManager;

  @Override
  public UserResponse create(SignUpRequest request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new EmailNotFoundException("User has already exist.");
    }
    Role roles = roleRepository.findByName(ROLE_ADMIN)
        .orElseThrow(() -> new RoleNotFoundException("User's Role not set."));
    User user = userRepository.save(new User(
        request.username(),
        passwordEncoder.encode(request.password()),
        request.email(),
        request.zipCode(),
        request.city(),
        Collections.singleton(roles)
    ));
    return new UserResponse(user.getUsername(), user.getEmail(), user.getZipCode(), user.getCity());
  }

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

  @Override
  public AccessToken getAccessToken(String email, String password) {
    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(email, password));
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return jwtProvider.generateToken(authentication);
  }
}
