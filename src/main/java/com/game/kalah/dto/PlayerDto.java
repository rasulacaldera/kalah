package com.game.kalah.dto;

import com.game.kalah.constants.PlayerId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDto {
    private PlayerId playerId;
    private String name;
}
