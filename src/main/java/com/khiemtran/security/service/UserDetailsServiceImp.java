package com.khiemtran.security.service;

import com.khiemtran.exception.EmailNotFoundException;
import com.khiemtran.model.User;
import com.khiemtran.repository.UserRepository;
import com.khiemtran.security.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        .orElseThrow(() -> new EmailNotFoundException("User not found username: " + email));
    return new UserPrincipal(user);
  }
}
