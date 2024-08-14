package com.khiemtran.service.impl;

import com.khiemtran.exception.ResourceNotFoundException;
import com.khiemtran.model.Token;
import com.khiemtran.repository.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = TokenServiceImpl.class)
@ActiveProfiles("test")
@SpringBootTest
class TokenServiceImplTest {
  @InjectMocks
  private TokenServiceImpl tokenService;
  @MockBean
  private TokenRepository tokenRepository;
  private Token existingToken;
  private Token newToken;

  @BeforeEach
  public void init() {
    ReflectionTestUtils.setField(tokenService, "tokenRepository", tokenRepository);
    newToken = new Token();
    newToken.setEmail("user@example.com");
    newToken.setAccessToken("newAccessToken");
    newToken.setRefreshToken("newRefreshToken");

    existingToken = new Token();
    existingToken.setEmail("user@example.com");
    existingToken.setAccessToken("existingAccessToken");
    existingToken.setRefreshToken("existingRefreshToken");
  }

  @Test
  public void saveToken_existingTokenUpdated() {
    Mockito.when(tokenRepository.findByEmail(anyString())).thenReturn(Optional.of(existingToken));
    tokenService.save(newToken);
    assertEquals("newAccessToken", existingToken.getAccessToken());
    assertEquals("newRefreshToken", existingToken.getRefreshToken());
    verify(tokenRepository).save(existingToken);
    verify(tokenRepository).save(newToken);
  }

  @Test
  public void saveToken_newTokenSaved() {
    Mockito.when(tokenRepository.findByEmail(anyString())).thenReturn(Optional.empty());
    tokenService.save(newToken);
    verify(tokenRepository).save(newToken);
  }

  @Test
  public void deleteToken_existingTokenDeleted() {
    when(tokenRepository.findByEmail(anyString())).thenReturn(Optional.of(existingToken));
    doNothing().when(tokenRepository).delete(existingToken);
    tokenService.delete("user@example.com");
    verify(tokenRepository).findByEmail("user@example.com");
    verify(tokenRepository).delete(existingToken);
  }

  @Test
  public void deleteToken_tokenNotFoundThrowsException() {
    when(tokenRepository.findByEmail(anyString())).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> tokenService.delete("user@example.com"));
    verify(tokenRepository, never()).delete(existingToken);
  }

  @Test
  public void isTokenLoggedOut_tokenExistsReturnsFalse() {
    when(tokenRepository.existsByEmail(anyString())).thenReturn(true);
    boolean result = tokenService.isTokenLoggedOut("user@example.com");
    assertFalse(result); // Token exists, so the result should be false
    Mockito.verify(tokenRepository).existsByEmail("user@example.com");
  }

  @Test
  public void isTokenLoggedOut_tokenDoesNotExistReturnsTrue() {
    when(tokenRepository.existsByEmail(anyString())).thenReturn(false);
    boolean result = tokenService.isTokenLoggedOut("user@example.com");
    assertTrue(result); // Token does not exist, so the result should be true
    Mockito.verify(tokenRepository).existsByEmail("user@example.com");
  }
}