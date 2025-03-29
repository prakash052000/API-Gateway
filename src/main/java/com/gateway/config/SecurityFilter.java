package com.gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class SecurityFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Bypass actuator endpoints
        if (exchange.getRequest().getURI().getPath().startsWith("/actuator")) {
            return chain.filter(exchange);
        }

        HttpHeaders headers = exchange.getRequest().getHeaders();

        // Check if Authorization token is present
        if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
            exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Proceed if security checks pass
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;  // Ensure filter executes first
    }
}
