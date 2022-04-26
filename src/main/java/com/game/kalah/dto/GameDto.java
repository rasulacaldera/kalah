package com.game.kalah.dto;

import lombok.Data;

import java.util.List;

@Data
public class GameDto {

    private Integer gameId;
    private List<PlayerDto> playerDtos;
    private List<BucketDto> bucketDtos;
}
