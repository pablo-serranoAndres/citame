package com.milibrodereservas.citame.auth;

import com.milibrodereservas.citame.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtUtil {
    @Autowired
    private SecretKey secretKey;

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("usermail", user.getEmail());
        claims.put("userphone", user.getPhone());
        claims.put("username", user.getName());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora
                .signWith(secretKey, SignatureAlgorithm.HS256) // IMPORTANTE: clave y algoritmo
                .compact();
    }
}
