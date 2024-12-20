
package com.is.mindart.security.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;


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
    @Value("${jwt.expiration}")
    private long expirationTerapeuta;

    public String generateToken(String username) {
        Date now = new Date();

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationTerapeuta))
                .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
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

    //voglio far scadere il token del bambino
    public void expirationBambino(String token) {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token);
        Date expiration = claims.getBody().getExpiration();
        Date now = new Date();
        if (now.after(expiration)) {
            throw new JwtException("Token scaduto");
        }
    }
}

