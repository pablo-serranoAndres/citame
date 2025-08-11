package com.milibrodereservas.citame.controllers;

import com.milibrodereservas.citame.global.Base;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Ping extends Base {
    @GetMapping("/citame/public/ping")
    public String getPing() {
        logger.info("GET /citame/public/ping");
        return "pong";
    }

    @GetMapping("/citame/priv/ping")
    public String getPrivatePing(@AuthenticationPrincipal String userName) {
        logger.info("GET /citame/priv/ping");
        return "pong " + userName;
    }
}
