package com.milibrodereservas.citame.controllers;

import com.milibrodereservas.citame.auth.CustomUserDetails;
import com.milibrodereservas.citame.global.Base;
import com.milibrodereservas.citame.model.LoginReply;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Ping", description = "Prueba de conectividad y autenticación")
public class Ping extends Base {
    @GetMapping("/citame/public/ping")
    @Operation(summary = "ping público para comprobar conectividad",
            description = "Devuelve pong")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Respuesta exitosa (pong)",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(type = "string", example = "pong")
                    )
            )
    })
    public String getPing() {
        logger.info("GET /citame/public/ping");
        return "pong";
    }

    @GetMapping("/citame/priv/ping")
    @Operation(summary = "ping privado para comprobar autenticación",
            description = "Devuelve pong si el token es válido",
            security = { @SecurityRequirement(name = "bearerAuth") } )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Respuesta exitosa (pong user)",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(type = "string", example = "pong guest")
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "No autorizado",
                    content = @Content(schema = @Schema(hidden = true)) // <-- sin body
            )
    })
    public String getPrivatePing(@AuthenticationPrincipal CustomUserDetails user) {
        logger.info("GET /citame/priv/ping");
        return "pong " + user.getUsername();
    }
}
