package com.example.client.controllers;

import com.example.client.model.Game;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/game")
public class RequestResponseController {
    private final RSocketRequester rSocketRequester;

    public RequestResponseController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    @GetMapping("/{id}")
    public Mono<Game> getGame(@PathVariable Long id) {
        return rSocketRequester
                .route("getGame")
                .data(id)
                .retrieveMono(Game.class);
    }

    @PostMapping
    public Mono<Game> addGame(@RequestBody Game game) {
        return rSocketRequester
                .route("addGame")
                .data(game)
                .retrieveMono(Game.class);
    }
}
