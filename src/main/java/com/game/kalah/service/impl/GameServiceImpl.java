package com.game.kalah.service.impl;

import com.game.kalah.dto.CreateGameRequestModel;
import com.game.kalah.dto.GameDto;
import com.game.kalah.service.GameService;

public class GameServiceImpl implements GameService {

    @Override
    public GameDto createGame(CreateGameRequestModel gameRequest) {
        GameDto game = new GameDto();

        return game;
    }
}
