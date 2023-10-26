package com.example.server.controller;

import com.example.server.model.Game;
import com.example.server.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class GameController {
    @Autowired
    private GameRepository gameRepository;
    @MessageMapping("getGame")
    public Mono<Game> getGame(Long id){
        return Mono.justOrEmpty(gameRepository.findById(id));
    }
    @MessageMapping("addGame")
    public Mono<Game> addGame(Game game){
        return Mono.justOrEmpty(gameRepository.save(game));
    }

    @MessageMapping("getAllGames")
    public Flux<Game> getAllGames(){
        return Flux.fromIterable(gameRepository.findAll());
    }

    @MessageMapping("deleteGame")
    public Mono<Void> deleteGame(Long id){
        gameRepository.deleteById(id);
        return Mono.empty();
    }

    @MessageMapping("gameChannel")
    public Flux<Game> gameChanel(Flux<Game> gameFlux){
        return gameFlux.flatMap(game -> Mono.fromCallable(() -> gameRepository.save(game)))
                .collectList().flatMapMany(Flux::fromIterable);
    }
}
