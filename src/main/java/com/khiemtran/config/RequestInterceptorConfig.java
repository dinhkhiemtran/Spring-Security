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
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    Long startTime = System.currentTimeMillis();
    Optional.ofNullable(request.getRemoteAddr())
        .ifPresent(remoteAddress ->
            ThreadContext.put("Remote_Address", "using " + remoteAddress));
    request.setAttribute(START_TIME, startTime);
    log.info("Request Started: [Method:{}, RequestId:{}, URI:{}, Timestamp:{}]",
        request.getMethod(),
        request.getRequestId(), request.getRequestURI(), startTime);
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    Long endTime = System.currentTimeMillis();
    Long attribute = (Long) request.getAttribute(START_TIME);
    Optional.ofNullable(attribute)
        .ifPresent(startTime -> {
          log.info("Complete Request [Status code:{}, Timestamp:{}, Duration:{}ms]", response.getStatus(), endTime, endTime - startTime);
        });
  }
}
