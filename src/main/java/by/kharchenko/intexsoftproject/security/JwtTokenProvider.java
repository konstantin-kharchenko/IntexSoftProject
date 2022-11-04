package by.kharchenko.intexsoftproject.security;

import by.kharchenko.intexsoftproject.model.entity.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String accessSecret;
    private final String refreshSecret;
    private final long lifetimeAccessToken;
    private final long lifetimeRefreshToken;


    @Autowired
    public JwtTokenProvider(@Value("${jwt.token.access.secret-key}") String accessSecret, @Value("${jwt.token.refresh.secret-key}") String refreshSecret, @Value("${lifetime.access.token}")  long lifetimeAccessToken, @Value("${lifetime.refresh.token}") long lifetimeRefreshToken) {
        this.accessSecret = Base64.getEncoder().encodeToString(accessSecret.getBytes());
        this.refreshSecret = Base64.getEncoder().encodeToString(refreshSecret.getBytes());
        this.lifetimeAccessToken = lifetimeAccessToken;
        this.lifetimeRefreshToken = lifetimeRefreshToken;
    }

    public String createAccessToken(User user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(lifetimeAccessToken).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(accessExpiration)
                .claim("roles", user.getRoles())
                .signWith(SignatureAlgorithm.HS512, accessSecret)
                .compact();
    }

    public String createRefreshToken(User user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(lifetimeRefreshToken).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(accessExpirationInstant);

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(refreshExpiration)
                .signWith(SignatureAlgorithm.HS512, refreshSecret)
                .compact();
    }

    public Claims getAccessClaims(String token) {
        return getClaims(token, accessSecret);
    }

    public Claims getRefreshClaims(String token) {
        return getClaims(token, refreshSecret);
    }

    public Claims getClaims(String token, String secret) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token, JwtType type) {
        final String secret = type.equals(JwtType.ACCESS) ? accessSecret : refreshSecret;
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
