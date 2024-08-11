package com.khiemtran.service.impl;

import com.khiemtran.exception.ResourceNotFoundException;
import com.khiemtran.model.Token;
import com.khiemtran.repository.TokenRepository;
import com.khiemtran.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
  private final TokenRepository tokenRepository;

  @Override
  public void save(Token newToken) {
    tokenRepository.findByEmail(newToken.getEmail())
        .ifPresentOrElse(
            existingToken -> {
              existingToken.setAccessToken(newToken.getAccessToken());
              existingToken.setRefreshToken(newToken.getRefreshToken());
              tokenRepository.save(existingToken);
            },
            () -> tokenRepository.save(newToken)
        );
  }

  @Override
  public void delete(String email) {
    Token token = tokenRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Not found token."));
    tokenRepository.delete(token);
  }

  @Override
  public boolean isTokenLoggedOut(String email) {
    return !(tokenRepository.existsByEmail(email));
  }
}
