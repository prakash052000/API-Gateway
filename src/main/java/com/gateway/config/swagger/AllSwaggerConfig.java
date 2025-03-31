package com.gateway.config.swagger;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AllSwaggerConfig {
	@Bean
	public GroupedOpenApi userServiceApi() {
		return GroupedOpenApi.builder().group("user-service").pathsToMatch("/users/**").build(); 
	}

}
