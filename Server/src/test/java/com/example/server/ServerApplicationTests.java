package com.example.server;

import com.example.server.controller.GameController;
import com.example.server.model.Game;
import com.example.server.repository.GameRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ServerApplicationTests {

    @Test
    public void testGetGameById() {
        Game coffee = new Game();
        coffee.setId(1L);
        coffee.setGenre("Action");
        GameRepository gameRepository = Mockito.mock(GameRepository.class);
        when(gameRepository.findById(1L)).thenReturn(Mono.just(coffee));
        GameController gameController = new GameController(gameRepository);
        ResponseEntity<Game> response = gameController.getGame(1L).block();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(coffee, response.getBody());
    }
    @Test
    public void testAddGame() {
        Game game = new Game();
        game.setId(1L);
        game.setGenre("Action");
        GameRepository gameRepository= Mockito.mock(GameRepository.class);
        when(gameRepository.save(game)).thenReturn(Mono.just(game));
        GameController gameController = new GameController(gameRepository);
        Mono<Game> response = gameController.addGame(game);
        assertEquals(game, response.block());
    }
}