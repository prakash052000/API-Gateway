package com.gateway.config;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SwaggerConfig {

    private final DiscoveryClient discoveryClient;

    public SwaggerConfig(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Bean
    public List<GroupedOpenApi> apis() {
        List<String> services = discoveryClient.getServices();

        return services.stream()
                .filter(service -> !service.equals("api-gateway")) // Exclude API Gateway itself
                .map(service -> GroupedOpenApi.builder()
                        .group(service)
                        .pathsToMatch("/" + service + "/**")  // Match paths dynamically
                        .build())
                .collect(Collectors.toList());
    }
}
