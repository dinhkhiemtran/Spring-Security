package com.khiemtran.security.filter;

import com.khiemtran.security.domains.AccessTokenGoogleInfo;
import com.khiemtran.security.rest.GoogleRestAPI;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class CustomGoogleFilter extends OncePerRequestFilter {
  private final GoogleRestAPI googleRestAPI;

  public CustomGoogleFilter(GoogleRestAPI googleRestAPI) {
    this.googleRestAPI = googleRestAPI;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String code = request.getParameter("code");
    if (StringUtils.isNotBlank(code)){
      AccessTokenGoogleInfo accessTokenGoogleInfo = googleRestAPI.accessTokenGoogleInfo(code);
      if (accessTokenGoogleInfo != null) {
        log.info("Bear token: {}", accessTokenGoogleInfo.idToken());
      }
    } else{
      filterChain.doFilter(request, response);
    }
  }
}
