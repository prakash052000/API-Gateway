package com.gateway.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class GatewayIndexConfig {

    @Bean
    public RouterFunction<ServerResponse> gatewayIndex() {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("service", "ApiGateway");
        body.put("status", "UP");
        body.put("eureka", "http://localhost:8761");
        body.put("serverAdmin", "http://localhost:8081");
        body.put("register", "POST /AuthService/auth/register");
        body.put("login", "POST /AuthService/auth/login");
        body.put("me", "GET /OrchestrationService/me");
        body.put("demo", "GET /OrchestrationService/Demo/user");
        body.put("users", "GET /UserService/users/test");
        return RouterFunctions.route(RequestPredicates.GET("/"), request ->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(body));
    }
}
