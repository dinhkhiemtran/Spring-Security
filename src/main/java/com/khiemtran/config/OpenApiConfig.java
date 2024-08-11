package com.khiemtran.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration

public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI(@Value("${openapi.service.title}") String title,
                               @Value("${openapi.service.license.name}") String name,
                               @Value("${openapi.service.license.identifier}") String identifier,
                               @Value("${openapi.service.license.url}") String url,
                               @Value("${openapi.service.version}") String version,
                               @Value("${openapi.service.description}") String description) {
    License licenseInfo = new License();
    licenseInfo.setName(name);
    licenseInfo.setExtensions(Map.of("SPDX", "MIT",
        "OSI Approved", "Yes"));
    licenseInfo.setIdentifier(identifier);
    licenseInfo.setUrl(url);
    return new OpenAPI()
        .info(new Info()
            .title(title)
            .version(version)
            .description(description)
            .license(licenseInfo))
        .servers(List.of(new Server()
            .url("http://localhost:8080/")))
        .components(new Components()
            .addSecuritySchemes("bearerAuth",
                new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")))
        .security(List.of(new SecurityRequirement()
            .addList("bearerAuth")));
  }

  @Bean
  public GroupedOpenApi groupedOpenApi() {
    return GroupedOpenApi.builder()
        .group("api-service-v1")
        .packagesToScan("com.khiemtran.controller")
        .build();
  }
}


