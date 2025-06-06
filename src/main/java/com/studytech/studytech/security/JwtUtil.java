package com.studytech.studytech.security;

import com.studytech.studytech.persistence.model.UserDocument;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    // SecretKey necesaria para generar el token
    private final SecretKey secretKey = Jwts.SIG.HS256.key().build();

    // Si mas adelante se crea RefreshToken, se crea otro metodo similar con la expiration modificada
    public String generateAccessToken(UserDocument user) {
        return generateToken(getExtraClaims(user), user, new Date(System.currentTimeMillis() + 3600000));
    }

    // Aca definimos en un map si queremos agregar mas datos al 'Claims' del token
    // Este metodo es OPCIONAL
    public Map<String, Object> getExtraClaims(UserDocument user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getName());
        extraClaims.put("role", user.getRole().toString());
        extraClaims.put("email", user.getEmail());
        return extraClaims;
    }

    // Construimos el token
    private String generateToken(Map<String, Object> extraClaims, UserDocument user, Date expiration) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    // Esta clase tambien valida si el token es correcto
    // De lo contrario devuelve diferentes excepciones
    public Claims getAllClaimsAndValidate(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(authToken);
            return true;
        } catch (SignatureException ex) {
            System.err.println("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            System.err.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            System.err.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            System.err.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.err.println("JWT claims string is empty");
        }
        return false;
    }
}