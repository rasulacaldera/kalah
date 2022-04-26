package com.game.kalah.dto;

import lombok.Data;

import java.util.List;

@Data
public class Game {

    private Integer gameId;
    private List<Player> players;
    private List<Bucket> buckets;
}
