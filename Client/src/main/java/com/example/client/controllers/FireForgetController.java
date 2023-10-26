package com.example.client.controllers;

import org.reactivestreams.Publisher;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
public class FireForgetController {
    private final RSocketRequester rSocketRequester;

    public FireForgetController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    @DeleteMapping("/{id}")
    public Publisher<Void> deleteGame(@PathVariable Long id) {
        return rSocketRequester
                .route("deleteGame")
                .data(id)
                .send();
    }
}
