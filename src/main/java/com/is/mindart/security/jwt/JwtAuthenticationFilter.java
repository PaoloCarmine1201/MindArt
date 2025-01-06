package com.is.mindart.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;
import java.util.Collections;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Il servizio per la gestione dei token JWT expired.
     */
    @Autowired
    private final FileBasedTokenBlacklist tokenBlacklist;

    /**
     * Questo filtro si occupa di estrarre
     * il token JWT dall'header Authorization,
     * validarlo e autenticare l'utente.
     */
    private final JwtUtil jwtUtil;


    /**
     * Questo metodo si occupa di estrarre il token JWT
     * dall'header Authorization, validarlo e autenticare l'utente.
     * @param request La richiesta HTTP
     * @param response La risposta HTTP
     * @param filterChain Il chain dei filtri
     * @throws ServletException Se si verifica
     * un errore durante la gestione della richiesta
     * @throws IOException Se si verifica un errore di I/O
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain)
            throws ServletException, IOException {
        final int bearerLength = 7;
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(bearerLength);

            if (jwtUtil.validateToken(token)) {
                if (tokenBlacklist.isTokenBlacklisted(token)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                String subject = jwtUtil.getUsernameFromToken(token);
                String role = jwtUtil.extractClaim(token, "role");
                if (role.contains("TERAPEUTA") || role.contains("BAMBINO")) {

                    SimpleGrantedAuthority authority =
                            new SimpleGrantedAuthority("ROLE_" + role);

                    // Creiamo l’oggetto Authentication
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    subject,
                                    null,
                                    Collections.singleton(authority)
                            );
                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
