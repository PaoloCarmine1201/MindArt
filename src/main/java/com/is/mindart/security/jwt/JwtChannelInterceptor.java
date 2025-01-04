package com.is.mindart.security.jwt;

import lombok.AllArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

@AllArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    /**
     * JwtUtil
     */
    private final JwtUtil jwtUtil;


    /**
     * Interceptor che si occupa di estrarre il token JWT
     * dall'header di una richiesta di connessione e di
     * autenticare l'utente ad un socket
     * @param message Il messaggio
     * @param channel Il canale di comunicazione
     * @return Il messaggio
     */
    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(
                message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authorization = accessor.getFirstNativeHeader("Authorization");
            if (authorization != null && authorization.startsWith("Bearer ")) {

                String token = authorization.substring(7);
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
        return message;
    }

}
