package com.game.kalah.rules.impl;

import com.game.kalah.constants.BucketType;
import com.game.kalah.constants.ErrorMessage;
import com.game.kalah.constants.GameStatus;
import com.game.kalah.constants.PlayerIndex;
import com.game.kalah.dto.ApiError;
import com.game.kalah.dto.BucketDto;
import com.game.kalah.dto.GameDto;
import com.game.kalah.dto.PlayerDto;
import com.game.kalah.exception.CustomServiceException;
import com.game.kalah.rules.GameRule;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameEndRule implements GameRule {

    @Override
    public BucketDto apply(GameDto game, BucketDto currentBucket) {

        Map<PlayerIndex, Integer> countPerPlayer = game.getBuckets()
                .stream()
                .filter(bucket -> bucket.getType().equals(BucketType.PIT))
                .collect(Collectors.groupingBy(BucketDto::getOwner, Collectors.summingInt(BucketDto::getStoneCount)));

        if (countPerPlayer.containsValue(0)) { //todo use numeric constant
            game.setGameStatus(GameStatus.FINISHED);
            PlayerIndex winnerId = getWinnerId(game);
            Optional<PlayerDto> winner = game.getPlayers()
                    .stream()
                    .filter(player -> player.getPlayerIndex().equals(winnerId))
                    .findFirst();
            winner.ifPresent(game::setWinner);
        }

        return null;
    }

    private PlayerIndex getWinnerId(GameDto game) {

        Optional<BucketDto> winningBucket = game.getBuckets()
                .stream()
                .filter(bucket -> bucket.getType().equals(BucketType.HOUSE))
                .max(Comparator.comparing(BucketDto::getStoneCount));

        if (winningBucket.isEmpty()) {
            CustomServiceException
                    .builder()
                    .error(new ApiError(ErrorMessage.UNABLE_TO_DETERMINE_WINNER));
        }
        return winningBucket.get().getOwner();
    }
}
