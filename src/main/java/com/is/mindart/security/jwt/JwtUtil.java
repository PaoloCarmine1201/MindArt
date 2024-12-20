
package com.is.mindart.security.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    /**
     *  Chiave segreta, salt
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     *  Tempo prima che scada la sessione del terapeuta
     */
    @Value("${jwt.expiration.terapeuta}")
    private long expirationTerapeuta;

    /**
     *  Tempo prima che scada la sessione del bambino
     */
    @Value("${jwt.expiration.bambino}")
    private long expirationBambino;

    public String generateToken(String username) {
        Date now = new Date();

        if (username.contains("@")) {
            return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(now)
                    .setExpiration(new Date(now.getTime() + expirationTerapeuta))
                    .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                    .compact();
        } else {
            return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(now)
                    .setExpiration(new Date(now.getTime() + expirationTerapeuta))
                    .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                    .compact();
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }
}

