package com.game.kalah.repository;

import com.game.kalah.dto.GameDto;

public interface GameRepository {

    GameDto save(GameDto game);

    boolean gameExists(String gameId);

    GameDto getById(String gameId);

}
