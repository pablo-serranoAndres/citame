package com.milibrodereservas.citame.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginReply {
    @Schema(description = "Token JWT para autenticación", example = "eyJhbGciOiJIUzI1NiIs...")
    private String token;
}
