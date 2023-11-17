package com.example.statki.config;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

@Configuration
public class JwtConfig {

    private static final String SECRET_KEY = "f45d2dxsA7s1xa7643h5hnegfjdsaut983u2ifjskajij4583auifjswiaji24";

    @Bean
    public JwtParser jwtParser() {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build();
    }

    @Bean
    public Key getSigningKey() {
        byte[] apiKeySecretBytes = Base64.getEncoder().encode(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }
}
