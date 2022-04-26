package com.game.kalah.repository.impl;

import com.game.kalah.dto.GameDto;
import com.game.kalah.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GameRepositoryInMemoryImpl implements GameRepository {

    private final Map<String, GameDto> games = new HashMap<>();

    @Override
    public GameDto save(GameDto game) {
        if (gameExists(game.getGameId())) {
            games.replace(game.getGameId(), game);
        }
        games.put(game.getGameId(), game);
        return game;
    }

    @Override
    public boolean gameExists(String gameId) {
        return games.containsKey(gameId);
    }

    @Override
    public GameDto getById(String gameId) {
        return games.get(gameId);
    }
}
