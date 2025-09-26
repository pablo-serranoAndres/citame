package com.milibrodereservas.citame.services;

import com.milibrodereservas.citame.auth.JwtUtil;
import com.milibrodereservas.citame.entities.User;
import com.milibrodereservas.citame.global.Base;
import com.milibrodereservas.citame.model.UserDto;
import com.milibrodereservas.citame.model.UserRegisterRequest;
import com.milibrodereservas.citame.repositories.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class UserService extends Base {
    public static final int FIELD_EMAIL_OR_PHONE = 1;
    public static final int FIELD_EMAIL = 2;
    public static final int FIELD_PHONE = 4;
    public static final int FIELD_NAME = 8;
    public static final int FIELD_PASSWORD = 16;

    @Autowired
    private UserRepository repo;
    @Autowired
    private JwtUtil jwtUtil;

    public UserDto findById(Long id) {
        try {
            User entity = repo.findById(id).orElseThrow();
            return new UserDto(entity);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public String login (String userName, String password) {
        logger.debug("UserService.login {}", userName);
        User user = repo.findByEmailOrPhone(userName, userName).orElse(null);
        if (user != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            // Verificar (en login):
            boolean matches = encoder.matches(password, user.getPassword());
            if (matches) {
                logger.debug("aceptado");
                return jwtUtil.generateToken(user);
            }
        }
        logger.debug("denegado");
        return null;
    }

    @Transactional
    public UserDto register (UserRegisterRequest user) throws ValidationException {
        logger.debug("UserService.register {} - {}", user.getEmail(), user.getPhone());
        if (!StringUtils.isBlank(user.getEmail())
                && repo.findByEmailOrPhone(user.getEmail(), user.getEmail())
                .orElse(null) != null) {
            throw new ValidationException("email already registered",
                    ValidationException.DUPLICATED, "email");
        }
        if (!StringUtils.isBlank(user.getPhone())
                && repo.findByEmailOrPhone(user.getPhone(), user.getPhone())
                .orElse(null) != null) {
            throw new ValidationException("phone already registered",
                    ValidationException.DUPLICATED, "phone");
        }

        User entity = new User();
        entity.setEmail(user.getEmail().trim());
        entity.setPhone(user.getPhone().trim());
        entity.setName(user.getName().trim());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(user.getPassword());
        entity.setPassword(hashedPassword);

        entity = repo.save(entity);
        if (entity == null) {
            throw new ValidationException("error persisting entity", ValidationException.DB_GENERIC_ERROR, "");
        }
        return new UserDto(entity);
    }
}
