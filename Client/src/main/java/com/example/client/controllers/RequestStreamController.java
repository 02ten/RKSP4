package com.example.client.controllers;


import com.example.client.model.Game;
import org.reactivestreams.Publisher;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class RequestStreamController {
    private final RSocketRequester rSocketRequester;

    public RequestStreamController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    @GetMapping
    public Publisher<Game> getAllGames() {
        return rSocketRequester
                .route("getAllGames")
                .data(new Game())
                .retrieveFlux(Game.class);
    }
}