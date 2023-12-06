package com.example.server.repository;

import com.example.server.model.Game;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface GameRepository extends ReactiveCrudRepository<Game, Long> {
}
