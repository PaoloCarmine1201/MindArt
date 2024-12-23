
package com.is.mindart.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

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
     *  Tempo prima che scada
     *  la sessione del terapeuta
     */
    @Value("${jwt.expiration}")
    private long expirationTerapeuta;

    public String generateToken(String username,String role) {
        Date now = new Date();

        return Jwts.builder()
                .claim("role", role)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationTerapeuta))
                .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    /**
     * Metodo per ottenere l'username dal token
     * @param token Il token
     * @return L'username
     */
    public String getUsernameFromToken(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(io.jsonwebtoken.security.Keys
                        .hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Metodo per ottenere il ruolo dal
     * token
     * @param token Il token da validare
     * @return se il token è valido o meno
     */
    public boolean validateToken(final String token) {
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

    public Claims extractAllClaims(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractClaim(final String token, final String claimKey) {
        Claims claims = extractAllClaims(token);
        return claims.get(claimKey, String.class); // Ritorna il valore del claim specifico
    }

}

