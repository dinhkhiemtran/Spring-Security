package com.khiemtran.service.impl;

import com.khiemtran.exception.EmailNotFoundException;
import com.khiemtran.model.User;
import com.khiemtran.repository.UserRepository;
import com.khiemtran.utils.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new EmailNotFoundException("User not found email: " + email, HttpStatus.UNAUTHORIZED));
    return new UserPrincipal(user);
  }
}
