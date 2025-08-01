package com.milibrodereservas.citame.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Ping {
    @GetMapping("/citame/public/ping")
    public String getPing() {
        return "pong";
    }
}
