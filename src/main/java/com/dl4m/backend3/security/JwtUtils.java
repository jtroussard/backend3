package com.dl4m.backend3.security;

import com.dl4m.backend3.config.JwtApplicationProperties;
import com.dl4m.backend3.config.cloud.SecretManagerUtil;
import com.dl4m.backend3.utils.CloudUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtils {

    @Getter
    private final long expiration;
    private final String secret;

    public JwtUtils(
            SecretManagerUtil secretManagerUtil,
            JwtApplicationProperties jwtProperties,
            CloudUtils cloudUtils
    ) {
        this.expiration = jwtProperties.getExpiration();

        if (cloudUtils.isRunningInGCP()) {
            this.secret = secretManagerUtil.getSecret("jwt-secret-dev");
        } else {
            // Local deployment sets the secret in the prop file directly via a .env file
            this.secret = jwtProperties.getSecret();
        }
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(CustomUserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(role -> role.getAuthority())
                .collect(Collectors.toList());

        log.debug("[{}] Generated JWT for user {}", this.getClass().getSimpleName(), userDetails.getUsername());
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", roles)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("[{}] Token validation failed: {}", this.getClass().getSimpleName(), e.getMessage());
            return false;
        }
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
