package com.is.mindart.security.jwt;

import com.is.mindart.security.service.BambinoUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Questo filtro si occupa di estrarre il token JWT dall'header Authorization,
     * validarlo e autenticare l'utente.
     */
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Questo UserDetailsService è usato per caricare l'utente dal token.
     * In questo esempio, usiamo un solo UserDetailsService per entrambi i tipi di utente.
     * In un'applicazione reale, potresti avere due UserDetailsService separati per Terapeuta e Bambino.
     */
    @Autowired
    private UserDetailsService terapeutaUserDetailsService;
    @Autowired
    private BambinoUserDetailsService bambinoUserDetailsService;

    // In caso di bambino, potremmo dover gestire diversamente.
    // Tuttavia, una volta validato il token,
    // potremmo considerare che il token può appartenere
    // sia a un bambino che a un terapeuta.
    // In questo esempio semplifichiamo assumendo che il token
    // identifichi l'utente solo con username,
    // potresti distinguere i due flussi con prefissi o ruoli nel token.

    /**
     * Questo metodo si occupa di estrarre il token JWT
     * dall'header Authorization, validarlo e autenticare l'utente.
     * @param request La richiesta HTTP
     * @param response La risposta HTTP
     * @param filterChain Il chain dei filtri
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if(header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            try {
                username = jwtUtil.getUsernameFromToken(token);
            } catch (Exception e) {
                // token non valido
            }
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carichiamo l'utente dal TerapeutaUserDetailsService:
            // In un caso reale, dovresti avere un modo per distinguere se username è un email (terapeuta)
            // o un codiceFiscale (bambino), magari memorizzando anche un claim nel token.

            // Per semplicità, supponiamo che se l'username contiene '@' è un terapeuta
            if(username.contains("@")) {
                var userDetails = terapeutaUserDetailsService
                        .loadUserByUsername(username);
                if(jwtUtil.validateToken(token)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities()
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource()
                            .buildDetails(request));
                    SecurityContextHolder.getContext()
                            .setAuthentication(authToken);
                }
            } else {
                // È un bambino (username = codiceFiscale)
                // Dato che non abbiamo un bambinoUserDetailsService come UserDetailsService standard,
                // serve logica aggiuntiva. In questo caso, potremmo salvare nel token un claim custom
                // con "ruolo" o "tipoUtente".
                // Per semplicità tralasciamo e consideriamo che se non è un terapeuta, non autentichiamo.
                var userDetails = bambinoUserDetailsService
                        .loadBambinoByCodice(username);
                if (jwtUtil.validateToken(token)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities()
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource()
                            .buildDetails(request));
                    SecurityContextHolder.getContext()
                            .setAuthentication(authToken);
}
            }
        }

        filterChain.doFilter(request, response);
    }
}