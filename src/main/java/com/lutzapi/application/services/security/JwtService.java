package com.lutzapi.application.services.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lutzapi.application.dtos.JwtObjectDTO;
import com.lutzapi.domain.entities.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
@RequiredArgsConstructor
public class JwtService {
    private final ObjectMapper mapper;

    @Value("${lutzapi.jwt.secret-key}")
    private String secret;

    @Getter
    @Value("${lutzapi.jwt.expiration-time}")
    private long jwtExpiration;

    public JwtObjectDTO parseAndValidate(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String payload = JWT.require(algorithm)
                                .withIssuer("auth-api")
                                .build()
                                .verify(token)
                                .getPayload();
            return mapper.readValue(payload, JwtObjectDTO.class);
        } catch (JWTVerificationException exception) {
            System.out.println("Erro ao processar JWT");
            return null;
        } catch (JsonProcessingException exception) {
            System.out.println("Erro ao converter JWT para objeto");
            return null;
        }
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }
}
