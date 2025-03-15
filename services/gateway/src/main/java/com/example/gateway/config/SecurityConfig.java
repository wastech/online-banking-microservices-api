package com.example.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        logger.info("SecurityConfig is being applied...");
        serverHttpSecurity
           .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchange ->
                exchange.pathMatchers("/eureka/**", "/api/v1/accounts/**", "/api/v1/auth/**")
                    .permitAll()
                    .anyExchange()
                    .authenticated())
          .oauth2ResourceServer(spec -> spec.jwt(Customizer.withDefaults()));
        return serverHttpSecurity.build();
    }
}