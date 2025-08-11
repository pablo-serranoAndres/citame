package com.milibrodereservas.citame.auth;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

// Clase empleada para inyectar datos desde application.properties

@Configuration
public class JwtConfig {
    @Value("${jwttoken.secret-key}")
    private String secretKey;

    @Bean
    public SecretKey jwtSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
