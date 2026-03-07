package roller.playground.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import roller.playground.config.JwtConfig;
import roller.playground.entities.Role;
import roller.playground.entities.User;

import java.util.Date;

@Service
@AllArgsConstructor
public class JwtService {
    private final JwtConfig jwtConfig;

    public String generateRefreshToken(User user) {
        return generateToken(user, jwtConfig.getRefreshTokenExpiration());
    }

    public String generateAccessToken(User user) {
        return generateToken(user, jwtConfig.getAccessTokenExpiration());
    }

    private String generateToken(User user, long tokenExpiration) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .signWith(jwtConfig.getSecretKey())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            var claims = getClaims(token);

            return claims.getExpiration().after(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getUserIdFromToken(String token) {
        return Long.valueOf(getClaims(token).getSubject());
    }

    public Role getRoleFromToken(String token) {
        return Role.valueOf(getClaims(token).get("role", String.class));
    }
}
