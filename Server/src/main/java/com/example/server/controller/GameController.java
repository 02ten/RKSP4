package com.example.server.controller;

import com.example.server.CustomException;
import com.example.server.model.Game;
import com.example.server.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.*;
@RestController
public class GameController {
    private final GameRepository gameRepository;

    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }
    @GetMapping("/getGame/{id}")
    public Mono<ResponseEntity<Game>> getGame(@PathVariable Long id){
        return gameRepository.findById(id)
                .map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping("/addGame")
    public Mono<Game> addGame(@RequestBody Game game){
        System.out.println(game.toString());
        return gameRepository.save(game).
                onErrorResume(e-> Mono.error(new CustomException("Invalid data", e)));
    }

    @GetMapping("/getAllGames")
    public Flux<Game> getAllGames(){
        return gameRepository.findAll().onBackpressureLatest();
    }

    @DeleteMapping("/deleteGame/{id}")
    public Mono<Void> deleteGame(@PathVariable Long id){
        return gameRepository.deleteById(id);
    }

    @GetMapping("/filtered")
    public Flux<Game> getGamesByGenre(@RequestParam String genre){
        return gameRepository.findAll()
                .filter(game -> game.getGenre().equals(genre));
    }
}
