package com.milibrodereservas.citame.services;

import com.milibrodereservas.citame.auth.JwtUtil;
import com.milibrodereservas.citame.entities.User;
import com.milibrodereservas.citame.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private JwtUtil jwtUtil;

    public String login (String userName, String password) {
        User user = repository.findByEmailOrPhone(userName, userName).orElse(null);
        if (user != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            //String hashedPassword = encoder.encode(password);
            // Verificar (en login):
            boolean matches = encoder.matches(password, user.getPassword());
            if (matches) {
                return jwtUtil.generateToken(user);
            }
        }
        return null;
    }

}
