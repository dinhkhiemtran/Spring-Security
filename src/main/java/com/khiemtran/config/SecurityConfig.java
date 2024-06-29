package com.khiemtran.config;

import com.khiemtran.security.filter.JwtAuthenticationFilter;
import com.khiemtran.security.service.UserDetailsServiceImp;
import com.khiemtran.utils.SecretKeySecretUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
  private final UserDetailsServiceImp userDetailsServiceImp;
  private final YamlConfig yamlConfig;
  private final SecretKeySecretUtil secretKeySecretUtil;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    RequestMatcher[] pathAllowed = getRequestMatchers();
    return httpSecurity.cors(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth ->
            auth.requestMatchers(pathAllowed).permitAll()
                .anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults())
        .headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
        .exceptionHandling(exceptionHandlingConfigurer ->
            exceptionHandlingConfigurer.authenticationEntryPoint((request, response, authException) ->
                response.sendError(response.SC_UNAUTHORIZED, authException.getMessage())))
        .sessionManagement(sessionManagementConfigurer ->
            sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  private RequestMatcher[] getRequestMatchers() {
    return Arrays.stream(Optional.ofNullable(yamlConfig.getPathAllowed())
            .orElse(new String[0]))
        .map(AntPathRequestMatcher::antMatcher)
        .toArray(RequestMatcher[]::new);
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(userDetailsServiceImp, secretKeySecretUtil);
  }

  @Bean
  AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsServiceImp);
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return new ProviderManager(authenticationProvider);
  }
}



