package com.khiemtran.config;

import com.khiemtran.security.filter.JwtAuthenticationFilter;
import com.khiemtran.service.impl.UserDetailsServiceImp;
import com.khiemtran.utils.SecretKeySecretUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {
  private final String[] WHITE_LIST = new String[]{"/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**"};
  private final UserDetailsServiceImp userDetailsServiceImp;
  private final YamlConfig yamlConfig;
  private final SecretKeySecretUtil secretKeySecretUtil;

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
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    RequestMatcher[] pathAllowed = getRequestMatchers();
    return httpSecurity.cors(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(pathAllowed).permitAll()
            .anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults())
        .headers(headersConfigurer -> headersConfigurer
            .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
        .exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer
            .authenticationEntryPoint(getAuthenticationEntryPoint()))
        .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  private RequestMatcher[] getRequestMatchers() {
    return Arrays.stream(Optional.ofNullable(yamlConfig.getWhiteList())
            .orElse(new String[0]))
        .map(AntPathRequestMatcher::antMatcher)
        .toArray(RequestMatcher[]::new);
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    RequestMatcher[] whiteList = Arrays.stream(WHITE_LIST)
        .map(AntPathRequestMatcher::antMatcher)
        .toArray(RequestMatcher[]::new);
    return webSecurityCustomizer ->
        webSecurityCustomizer.ignoring()
            .requestMatchers(whiteList);
  }

  @Bean
  public AuthenticationEntryPoint getAuthenticationEntryPoint() {
    return (request, response, authException) ->
        response.sendError(response.SC_UNAUTHORIZED, authException.getMessage());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(userDetailsServiceImp, secretKeySecretUtil);
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsServiceImp);
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }
}



