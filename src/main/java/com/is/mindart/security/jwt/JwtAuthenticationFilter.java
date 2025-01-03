package com.is.mindart.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
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
     * Questo filtro si occupa di estrarre
     * il token JWT dall'header Authorization,
     * validarlo e autenticare l'utente.
     */
    private JwtUtil jwtUtil;


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

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            if (jwtUtil.validateToken(token)) {
                String subject = jwtUtil.getUsernameFromToken(token);
                String role = jwtUtil.extractClaim(token, "role");
                if (role.contains("TERAPEUTA") || role.contains("BAMBINO")) {

                    SimpleGrantedAuthority authority =
                            new SimpleGrantedAuthority("ROLE_" + role);

                    // Creiamo l’oggetto Authentication
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    subject,
                                    null, // no password
                                    Collections.singleton(authority)
                            );
                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
