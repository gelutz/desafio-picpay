package com.lutzapi.security.jwt;

import com.lutzapi.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${lutzapi.jwtSecret}")
    private String jwtSecret;

    @Value("${lutzapi.jwtExpirationMillis}")
    private int jwtExpirationMillis;

    @Value("${lutzapi.jwtCookieName}")
    private String jwtCookie;

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
        return ResponseCookie.from(jwtCookie, jwt)
                .path("/")
                .maxAge(24 * 60 * 60)
                .httpOnly(true)
                .build();
    }

    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtCookie, null)
                .path("/")
                .build();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(generate256BitKey()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public SecretKey generate256BitKey() {
        try {
            // Ensure the secret string is non-null and non-empty
            if (jwtSecret == null || jwtSecret.isEmpty()) {
                throw new IllegalArgumentException("Secret string is not set.");
            }

            // Convert the secret string to a byte array
            char[] secretBytes = jwtSecret.toCharArray();

            // Generate a salt
            SecureRandom secureRandom = new SecureRandom();
            byte[] salt = new byte[32];
            secureRandom.nextBytes(salt);

            // Use PBKDF2 with a high number of iterations to derive the key
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec keySpec = new PBEKeySpec(secretBytes, salt, 65536, 256); // 256 bytes
            return new SecretKeySpec(factory.generateSecret(keySpec).getEncoded(), "HmacSHA256");
        } catch (Exception e) {
            throw new RuntimeException("Error generating secret key", e);
        }
    }

    public boolean validateJwtToken(String authToken) {
        try {
            System.out.println(generate256BitKey().toString());
            Jwts.parserBuilder().setSigningKey(generate256BitKey()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMillis))
                .signWith(generate256BitKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}