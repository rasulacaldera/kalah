package com.game.kalah.dto;

import lombok.Data;

import java.util.List;

@Data
public class GameDto {

    private String gameId;
    private List<PlayerDto> players;
    private List<BucketDto> buckets;
}
