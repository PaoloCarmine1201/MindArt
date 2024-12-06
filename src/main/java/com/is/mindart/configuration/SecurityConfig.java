package com.is.mindart.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura la sicurezza dell'applicazione.
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception Eccezione
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            final HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(authz ->
                authz.anyRequest().permitAll());
        return http.build();
    }
}
