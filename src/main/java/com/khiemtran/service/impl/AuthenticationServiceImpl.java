package com.khiemtran.service.impl;

import com.khiemtran.dto.request.LoginRequest;
import com.khiemtran.dto.request.SignUpRequest;
import com.khiemtran.dto.response.AccessToken;
import com.khiemtran.dto.response.UserResponse;
import com.khiemtran.exception.EmailNotFoundException;
import com.khiemtran.exception.RoleNotFoundException;
import com.khiemtran.model.Role;
import com.khiemtran.model.User;
import com.khiemtran.repository.RoleRepository;
import com.khiemtran.repository.UserRepository;
import com.khiemtran.security.provider.JwtProvider;
import com.khiemtran.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static com.khiemtran.model.RoleName.ROLE_ADMIN;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtProvider jwtProvider;

  @Override
  public UserResponse register(SignUpRequest request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new EmailNotFoundException("User has already exist.");
    }
    Role roles = getRoles();
    User userEntity = new User(
        request.username(),
        passwordEncoder.encode(request.password()),
        request.email(),
        request.zipCode(),
        request.city(),
        Collections.singleton(roles));
    User user = userRepository.save(userEntity);
    return new UserResponse(user.getUsername(), user.getEmail(), user.getZipCode(), user.getCity());
  }

  @Override
  public AccessToken authenticate(LoginRequest loginRequest) {
    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
    userRepository.findByEmail(loginRequest.email()).orElseThrow(() -> new EmailNotFoundException("Email not found"));
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return jwtProvider.generateToken(authentication);
  }

  private Role getRoles() {
    return roleRepository.findByName(ROLE_ADMIN)
        .orElseThrow(() -> new RoleNotFoundException("User's Role not set."));
  }
}
