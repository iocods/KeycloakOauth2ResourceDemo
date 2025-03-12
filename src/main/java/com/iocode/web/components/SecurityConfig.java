package com.iocode.web.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtConverter jwtConverter;
    @Value("${jwkSetUri}")
    private String jwkSetUri;

    @Bean

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.oauth2ResourceServer(oauth2 -> {
            oauth2.jwt(jwt -> {
                jwt.jwkSetUri(jwkSetUri);
                jwt.jwtAuthenticationConverter(jwtConverter);
            });
        });

        http.authorizeHttpRequests(auth ->
            auth.requestMatchers("/hello")
                .permitAll()
                .anyRequest()
                .authenticated());
        return  http.build();
    }

}
