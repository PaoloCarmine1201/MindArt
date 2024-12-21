package com.is.mindart.security.jwt;

import com.is.mindart.security.service.BambinoUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;

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
     * Questo filtro si occupa di estrarre
     * il token JWT dall'header Authorization,
     * validarlo e autenticare l'utente.
     */
    private UserDetailsService terapeutaUserDetailsService;

    /**
     * Questo filtro si occupa di estrarre
     * il token JWT dall'header Authorization,
     * validarlo e autenticare l'utente.
     */
    private BambinoUserDetailsService bambinoUserDetailsService;

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
        String token = null;
        String username = null;

        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            try {
                username = jwtUtil.getUsernameFromToken(token);
            } catch (Exception e) {
                logger.error(
                        "Errore nell'estrazione dell'username dal token", e);
            }
        }
        if (username != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            String role = jwtUtil.extractClaim(token, "role");
            if (role.contains("TERAPEUTA")) {
                var userDetails = terapeutaUserDetailsService
                        .loadUserByUsername(username);
                if (jwtUtil.validateToken(token)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null,
                                    userDetails.getAuthorities()
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource()
                            .buildDetails(request));
                    SecurityContextHolder.getContext()
                            .setAuthentication(authToken);
                }
                if (role.contains("BAMBINO")) {
                    userDetails = bambinoUserDetailsService
                            .loadBambinoByCodice(username);

                    if (jwtUtil.validateToken(token)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null,
                                        userDetails.getAuthorities()
                                );

                        authToken.setDetails(
                                new WebAuthenticationDetailsSource()
                                .buildDetails(request));
                        SecurityContextHolder.getContext()
                                .setAuthentication(authToken);
                    }

                } else {
                    throw new IllegalStateException("Ruolo non riconosciuto");
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}