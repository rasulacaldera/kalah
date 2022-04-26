package com.game.kalah.service;

import com.game.kalah.dto.CreateGameRequestModel;
import com.game.kalah.dto.GameDto;

public interface GameService {

    GameDto createGame(CreateGameRequestModel gameRequest);
}
