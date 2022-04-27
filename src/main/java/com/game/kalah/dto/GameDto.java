package com.game.kalah.dto;

import com.game.kalah.constants.GameStatus;
import com.game.kalah.constants.PlayerIndex;
import lombok.Data;

import java.util.List;

@Data
public class GameDto {

    private String gameId;
    private List<PlayerDto> players;
    private GameStatus gameStatus;
    private PlayerDto winner;
    private PlayerIndex nextPlayer;
    private List<BucketDto> buckets;
}
