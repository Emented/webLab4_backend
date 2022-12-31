package com.emented.weblab4.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LifeCheckController {

    private static final String PONG_ANSWER = "pong";

    @GetMapping("/ping")
    private ResponseEntity<String> pong() {
        return ResponseEntity.ok().body(PONG_ANSWER);
    }

}
