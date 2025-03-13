package com.triptrek.triptrek.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service  // ✅ Zorg ervoor dat deze annotatie aanwezig is!
public class JwtService {
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            "MijnSuperGeheimeSleutelVoorJWTMet32Tekens!".getBytes()
    );

    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 uur geldig

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, Jwts.SIG.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        try {
            JwtParser parser = Jwts.parser().verifyWith(SECRET_KEY).build();
            Claims claims = parser.parseSignedClaims(token).getPayload();
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            System.out.println("Token is verlopen!");
            return null;
        } catch (Exception e) {
            System.out.println("Fout bij token-parsing: " + e.getMessage());
            return null;
        }
    }

    public boolean validateToken(String token, String email) {
        return (email.equals(extractEmail(token))) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        try {
            JwtParser parser = Jwts.parser().verifyWith(SECRET_KEY).build();
            Claims claims = parser.parseSignedClaims(token).getPayload();
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
