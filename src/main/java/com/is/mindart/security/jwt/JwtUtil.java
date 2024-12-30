
package com.is.mindart.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;

import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;


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
    private Long expiration;

    public String generateToken(final String username, final String role) {
        Date now = new Date();

        return Jwts.builder()
                .claim("role", role)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration))
                .signWith(hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    /**
     * Metodo per ottenere l'username dal token
     * @param token Il token
     * @return L'username
     */
    public String getUsernameFromToken(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(hmacShaKeyFor(secret.getBytes()))
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
                    .setSigningKey(hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }

    /**
     * Metodo per estrarre tutti i claim
     * dal token
     * @param token Il token
     * @return I claim
     */
    public Claims extractAllClaims(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Metodo per estrarre un claim specifico
     * dal token
     * @param token Il token
     * @param claimKey La chiave del claim
     * @return Il claim
     */
    public String extractClaim(final String token, final String claimKey) {
        Claims claims = extractAllClaims(token);
        // Ritorna il valore del claim specifico
        return claims.get(claimKey, String.class);
    }

}

