package com.example.client.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Game {
    private Long id;
    private String name;
    private Double price;
    private String genre;
}
