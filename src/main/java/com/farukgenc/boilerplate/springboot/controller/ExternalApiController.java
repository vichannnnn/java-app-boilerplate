package com.farukgenc.boilerplate.springboot.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import com.farukgenc.boilerplate.springboot.model.Cannabis;

@RestController
@RequestMapping("/api")
public class ExternalApiController {

    private final WebClient webClient;

    public ExternalApiController(WebClient webClient) {
        this.webClient = webClient;
    }

    
    @GetMapping("/cannabis")
    @PreAuthorize("isAuthenticated()")
    public Mono<ResponseEntity<Cannabis>> getRandomCannabis() {
        return webClient
            .get()
            .uri("https://random-data-api.com/api/cannabis/random_cannabis")
            .retrieve()
            .bodyToMono(Cannabis.class)
            .map(cannabis -> ResponseEntity.ok(cannabis));
}
}
