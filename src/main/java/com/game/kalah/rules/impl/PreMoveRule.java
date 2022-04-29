package com.game.kalah.rules.impl;

import com.game.kalah.constants.BucketType;
import com.game.kalah.constants.ErrorMessage;
import com.game.kalah.constants.GameStatus;
import com.game.kalah.constants.NumericConstants;
import com.game.kalah.dto.ApiError;
import com.game.kalah.dto.BucketDto;
import com.game.kalah.dto.GameDto;
import com.game.kalah.exception.CustomServiceException;
import com.game.kalah.rules.GameRule;
import org.springframework.stereotype.Service;

@Service
public class PreMoveRule implements GameRule {

    @Override
    public BucketDto apply(GameDto game, BucketDto currentBucket) {

        if (game.getGameStatus().equals(GameStatus.FINISHED)) {
            throw CustomServiceException
                    .builder()
                    .error(new ApiError(ErrorMessage.GAME_ALREADY_FINISHED))
                    .build();
        }

        if (!currentBucket.getOwner().equals(game.getNextPlayer())) {
            String playerName = game.getPlayers()
                    .stream()
                    .filter(player -> player.getPlayerId().equals(game.getNextPlayer()))
                    .findFirst()
                    .get()
                    .getName();
            throw CustomServiceException
                    .builder()
                    .error(new ApiError(ErrorMessage.WRONG_PLAYER_TURN, playerName))
                    .build();
        }

        if (currentBucket.getType().equals(BucketType.HOUSE)) {
            throw CustomServiceException
                    .builder()
                    .error(new ApiError(ErrorMessage.CANNOT_START_FROM_HOUSE))
                    .build();
        }

        if (currentBucket.getStoneCount() == NumericConstants.EMPTY_PIT_STONE_COUNT) {
            throw CustomServiceException
                    .builder()
                    .error(new ApiError(ErrorMessage.PIT_HAS_NO_STONES))
                    .build();
        }

        return currentBucket;
    }
}
