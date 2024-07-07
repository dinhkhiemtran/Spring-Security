package com.khiemtran.security.config;

import com.khiemtran.security.filter.CustomGoogleFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SystemConfig {
  private final CustomGoogleFilter customGoogleFilter;

  public SystemConfig(CustomGoogleFilter customGoogleFilter) {
    this.customGoogleFilter = customGoogleFilter;
  }

  @Bean
  FilterRegistrationBean<CustomGoogleFilter> filterRegistrationBean() {
    FilterRegistrationBean<CustomGoogleFilter> filterRegistrationBean = new FilterRegistrationBean<>();
    filterRegistrationBean.setFilter(customGoogleFilter);
    return filterRegistrationBean;
  }
}
