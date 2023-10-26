package com.example.server;

import com.example.server.model.Game;
import com.example.server.repository.GameRepository;
import io.rsocket.frame.decoder.PayloadDecoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ServerApplicationTests {

    @Autowired
    private GameRepository gameRepository;

    private RSocketRequester requester;

    @BeforeEach
    public void setup() {
        requester = RSocketRequester.builder()
                .rsocketStrategies(builder -> builder.decoder(new Jackson2JsonDecoder()))
                .rsocketStrategies(builder -> builder.encoder(new Jackson2JsonEncoder()))
                .rsocketConnector(connector -> connector
                        .payloadDecoder(PayloadDecoder.ZERO_COPY)
                        .reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2))))
                .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                .tcp("localhost", 7000);
    }

    @AfterEach
    public void cleanup() {
        requester.dispose();
    }

    @Test
    public void testGetGame() {
        Game game = new Game();
        game.setName("Bioshock");
        game.setPrice(399.99);
        game.setGenre("Action");

        Game savedGame = gameRepository.save(game);

        Mono<Game> result = requester.route("getGame")
                .data(savedGame.getId())
                .retrieveMono(Game.class);

        assertNotNull(result.block());
    }

    @Test
    public void testAddGame() {
        Game game = new Game();
        game.setName("Bioshock");
        game.setPrice(399.99);
        game.setGenre("Action");

        Mono<Game> result = requester.route("addGame")
                .data(game)
                .retrieveMono(Game.class);

        Game savedGame = result.block();
        assertNotNull(savedGame);
        assertNotNull(savedGame.getId());
        assertTrue(savedGame.getId() > 0);
    }

    @Test
    public void testGetAllGames() {
        Flux<Game> result = requester.route("getAllGames")
                .retrieveFlux(Game.class);
        assertNotNull(result.blockFirst());
    }

    @Test
    public void testDeleteGame() {
        Game game = new Game();
        game.setName("Bioshock");
        game.setPrice(399.99);
        game.setGenre("Action");

        Game savedGame = gameRepository.save(game);

        Mono<Void> result = requester.route("deleteGame")
                .data(savedGame.getId())
                .send();

        result.block();
        Game deletedGame = gameRepository.findById(savedGame.getId()).get();
        assertNotSame(deletedGame, savedGame);
    }

}