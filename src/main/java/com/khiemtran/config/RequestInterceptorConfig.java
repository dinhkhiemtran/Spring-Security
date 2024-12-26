package com.khiemtran.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
@Slf4j
public class RequestInterceptorConfig implements HandlerInterceptor {
  private static final String START_TIME = "start_time";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    long startTime = System.currentTimeMillis();
    Optional.ofNullable(request.getRemoteAddr())
        .ifPresent(remoteAddress -> ThreadContext.put("Remote_Address", remoteAddress));
    request.setAttribute(START_TIME, startTime);
    log.info("Request Started: [Method:{}, URI:{}, Timestamp:{}]",
        request.getMethod(), request.getRequestURI(), startTime);
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    long endTime = System.currentTimeMillis();
    Optional.ofNullable((Long) request.getAttribute(START_TIME))
        .ifPresent(startTime -> log.info("Request Completed: [Status:{}, Duration:{}ms]",
            response.getStatus(), endTime - startTime));
  }
}