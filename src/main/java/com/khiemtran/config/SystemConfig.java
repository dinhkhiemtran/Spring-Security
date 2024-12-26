package com.khiemtran.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

import static com.khiemtran.constant.Constants.WHITE_LIST;

@Configuration
@EnableJpaAuditing
@RequiredArgsConstructor
public class SystemConfig implements WebMvcConfigurer {
  private final RequestInterceptorConfig requestInterceptorConfig;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(requestInterceptorConfig).excludePathPatterns(WHITE_LIST);
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("**")
        .allowedOrigins("http://localhost:4200")
        .allowedMethods("GET", "POST", "PUT", "DELETE")
        .allowedHeaders("*")
        .allowCredentials(false)
        .maxAge(3600);
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    RequestMatcher[] whiteList = Arrays.stream(WHITE_LIST)
        .map(AntPathRequestMatcher::new)
        .toArray(RequestMatcher[]::new);
    return webSecurity -> webSecurity.ignoring().requestMatchers(whiteList);
  }
}