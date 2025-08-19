package com.milibrodereservas.citame.controllers;

import com.milibrodereservas.citame.global.Base;
import com.milibrodereservas.citame.model.errors.ErrorReply;
import com.milibrodereservas.citame.model.LoginReply;
import com.milibrodereservas.citame.model.LoginRequest;
import com.milibrodereservas.citame.model.UserRegisterRequest;
import com.milibrodereservas.citame.services.UserService;
import com.milibrodereservas.citame.services.ValidationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/citame/auth")
@Tag(name = "Autenticación", description = "Operaciones relacionadas con la autenticación")
public class AuthController extends Base {
    @Autowired
    private UserService service;

    @PostMapping("/login")
    @Operation(summary = "Login usuario",
            description = "Devuelve el token de autorización")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginReply.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario o contraseña incorrectas",
                    content = @Content(schema = @Schema(hidden = true)) // <-- sin body
            )
    })
    public ResponseEntity<LoginReply> login(@RequestBody LoginRequest loginRequest) {
        logger.info("POST /citame/auth/login {}", loginRequest.getUserName());
        final String token = service.login(loginRequest.getUserName(), loginRequest.getPassword());
        if (token != null) {
            logger.info("{} admitido", loginRequest.getUserName());
            return ResponseEntity.ok(new LoginReply(token));
        } else {
            logger.warn("{} rechazado", loginRequest.getUserName());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("register")
    @Operation(summary = "Registro usuario",
            description = "Devuelve el token de autorización")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario registrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginReply.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReply.class)
                    )
            )    })
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterRequest registerRequest) {
        logger.info("POST /citame/auth/register {} - {}", registerRequest.getEmail(),
                registerRequest.getPhone());
        try {
            service.register(registerRequest);
            String userName = registerRequest.getEmail();
            if (userName == null) {
                userName = registerRequest.getPhone();
            }
            final String token = service.login(userName, registerRequest.getPassword());
            if (token != null) {
                logger.info("{} registrado", userName);
                return ResponseEntity.ok(new LoginReply(token));
            } else {
                logger.warn("{} rechazado despues de registrar", userName);
                return ResponseEntity.notFound().build();
            }
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(new ErrorReply(e));
        }
    }
}
