
package com.is.mindart.configuration;

import com.is.mindart.security.jwt.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Questo componente si occupa di filtrare le richieste
     * e di estrarre il token JWT dall'header Authorization.
     */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    /**
     * Configura l'AuthenticationManager.
     * @param configuration AuthenticationConfiguration
     * @return AuthenticationManager
     * @throws Exception Se si verifica un errore durante la configurazione
     */
    @Bean
    public AuthenticationManager authenticationManager(
            final AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Configura le regole di sicurezza per l'applicazione.
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception Se si verifica un errore durante la configurazione
     */
    @Bean
    public SecurityFilterChain filterChain(
            final HttpSecurity http)
            throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/app/**").permitAll()
                        .requestMatchers("/topic/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/terapeuta/**")
                        .hasRole("TERAPEUTA")
                        .requestMatchers("/api/bambino/**")
                        .hasRole("BAMBINO")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        http.addFilterBefore(jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configura l'encoder per la password.
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
