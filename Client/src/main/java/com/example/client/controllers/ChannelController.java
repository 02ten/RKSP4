package com.example.client.controllers;

import com.example.client.model.Game;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/game")
public class ChannelController {
    private final RSocketRequester rSocketRequester;

    public ChannelController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }


    @PostMapping("/channel")
    public Flux<Game> addGames(@RequestBody List<Game> game){
        Flux<Game> gameFlux = Flux.fromIterable(game);
        return rSocketRequester
                .route("gameChannel")
                .data(gameFlux)
                .retrieveFlux(Game.class);
    }
}
