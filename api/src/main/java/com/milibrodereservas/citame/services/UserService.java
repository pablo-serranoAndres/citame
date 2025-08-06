package com.milibrodereservas.citame.services;

import com.milibrodereservas.citame.auth.JwtUtil;
import com.milibrodereservas.citame.entities.User;
import com.milibrodereservas.citame.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public String login (String userName, String password) {
        User user = repository.findByEmailOrPhone(userName, userName).orElse(null);
        if (user != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            //String hashedPassword = encoder.encode(password);
            // Verificar (en login):
            boolean matches = encoder.matches(password, user.getPassword());
            if (matches) {
                return JwtUtil.generateToken(user);
            }
        }
        return null;
    }

}
