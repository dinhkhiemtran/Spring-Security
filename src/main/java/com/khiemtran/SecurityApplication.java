package com.khiemtran;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing
@EntityScan(basePackages = "com.khiemtran.model")
public class SecurityApplication {
  @PostConstruct
  public void setup() {
    TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
  }

  public static void main(String[] args) {
    SpringApplication.run(SecurityApplication.class, args);
  }
}
