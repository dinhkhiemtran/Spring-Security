package com.khiemtran.service;

import com.khiemtran.model.Token;

public interface TokenService {
  void save(Token token);

  void delete(String email);

  boolean isTokenLoggedOut(String email);
}
