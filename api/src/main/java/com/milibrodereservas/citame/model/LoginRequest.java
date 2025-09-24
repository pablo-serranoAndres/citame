package com.milibrodereservas.citame.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginRequest {
    @Schema(description = "email o phone para autenticar", example = "guest@citame.com")
    private String userName;
    @Schema(description = "Password", example = "p4ssw0rd123&s3gur4")
    private String password;
}
