package com.projinteg.GesEvents.utils;

import com.projinteg.GesEvents.security.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public void printJwtSecret() {
        System.out.println("JWT Secret: " + jwtSecret);
    }

    // Method to generate JWT token
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        // Get the role dynamically from authorities
        String role = userPrincipal.getAuthorities()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Role not found"))
                .getAuthority()
                .replace("ROLE_", ""); // Removing "ROLE_" prefix

        // You can also include the email in the claims if needed
        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(userPrincipal.getUsername()) // This will be the username or email based on your logic
                .claim("role", role) // Dynamically add role
                .claim("email", userPrincipal.getEmail()) // Adding email as a claim if needed
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();

        logger.debug("Generated JWT Token: {}", token);
        return token;
    }

    // Method to get the signing key (used for JWT signature validation)
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // Retrieve username from JWT Token
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    // Retrieve roles from JWT Token
    public String getRoleFromJwtToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token).getBody();
        return claims.get("role", String.class); // Retrieves the role from the claims
    }

    // Retrieve email from JWT Token if included
    public String getEmailFromJwtToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token).getBody();
        return claims.get("email", String.class); // Retrieves the email from the claims
    }

    // Validate JWT Token
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    // Helper method to get the signing key (used for validating the JWT)
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // Get all claims from the JWT token
    public Claims getClaimsFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
