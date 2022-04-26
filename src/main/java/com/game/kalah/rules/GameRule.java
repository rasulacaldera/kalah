package com.game.kalah.rules;

import com.game.kalah.dto.GameDto;

public interface GameRule {

    void apply(GameDto game, Integer pitIndex);
}
