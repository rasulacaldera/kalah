package com.game.kalah.dto;

import com.game.kalah.constants.PlayerIndex;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Player {
    private PlayerIndex playerIndex;
    private String name;
}
