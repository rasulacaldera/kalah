package com.game.kalah.dto;

import com.game.kalah.constants.GameStatus;
import com.game.kalah.constants.PlayerId;
import lombok.Data;

import java.util.List;

@Data
public class GameDto {

    private String gameId;
    private List<PlayerDto> players;
    private GameStatus gameStatus;
    private PlayerDto winner;
    private PlayerId nextPlayer;
    private List<BucketDto> buckets;
}
