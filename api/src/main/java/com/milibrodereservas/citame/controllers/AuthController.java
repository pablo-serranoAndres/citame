package com.milibrodereservas.citame.controllers;

import com.milibrodereservas.citame.global.Base;
import com.milibrodereservas.citame.model.LoginRequest;
import com.milibrodereservas.citame.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/citame/auth")
public class AuthController extends Base {
    @Autowired
    private UserService service;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        logger.info("POST /citame/auth/login {}", loginRequest.getUserName());
        final String token = service.login(loginRequest.getUserName(), loginRequest.getPassword());
        if (token != null) {
            logger.info("{} admitido", loginRequest.getUserName());
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } else {
            logger.warn("{} rechazado", loginRequest.getUserName());
            return ResponseEntity.notFound().build();
        }
    }
}
