package com.odos.odos_server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GraphQLCorsConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping("/graphql")
        .allowedOrigins("https://studio.apollographql.com")
        .allowedMethods("GET", "POST")
        .allowedHeaders("*");
  }
}
