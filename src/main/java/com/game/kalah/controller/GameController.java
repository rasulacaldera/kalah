package com.game.kalah.controller;

import com.game.kalah.dto.CreateGameRequestModel;
import com.game.kalah.dto.GameDto;
import com.game.kalah.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/v1/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<GameDto> createGame(@RequestBody CreateGameRequestModel createGameRequest) {

        GameDto game = gameService.createGame(createGameRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(game);
    }

    @PutMapping("/{gameId}/pits/{pitIndex}")
    public ResponseEntity<GameDto> move(@PathVariable String gameId, @PathVariable Integer pitIndex) {

        GameDto game = gameService.makeMove(gameId, pitIndex);

        return ResponseEntity.status(HttpStatus.OK).body(game);
    }
}
