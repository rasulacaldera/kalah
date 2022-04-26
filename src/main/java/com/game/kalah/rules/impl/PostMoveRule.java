package com.game.kalah.rules.impl;

import com.game.kalah.dto.GameDto;
import com.game.kalah.rules.GameRule;
import org.springframework.stereotype.Service;

@Service
public class PostMoveRule implements GameRule {

    @Override
    public void apply(GameDto game, Integer pitIndex) {

        System.out.println(game);
    }
}
