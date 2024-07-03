package com.khiemtran.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class SystemConfig {
//  @Bean
//  public OpenAPI customOpenAPI() {
//    return new OpenAPI()
//        .info(new Info().title("API").version("1.0"))
//        .components(new Components()
//            .addSecuritySchemes("bearer-key",
//                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
//        .addSecurityItem(new SecurityRequirement().addList("bearer-key"));
//  }
}
