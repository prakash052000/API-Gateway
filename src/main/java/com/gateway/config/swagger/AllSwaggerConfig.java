package com.gateway.config.swagger;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AllSwaggerConfig {

    @Bean
    public GroupedOpenApi authServiceApi() {
        return GroupedOpenApi.builder().group("AuthService").pathsToMatch("/AuthService/**", "/auth/**", "/jwt/**").build();
    }

    @Bean
    public GroupedOpenApi orchestrationServiceApi() {
        return GroupedOpenApi.builder().group("OrchestrationService").pathsToMatch("/OrchestrationService/**").build();
    }

    @Bean
    public GroupedOpenApi userServiceApi() {
        return GroupedOpenApi.builder().group("UserService").pathsToMatch("/UserService/**", "/users/**").build();
    }
}
