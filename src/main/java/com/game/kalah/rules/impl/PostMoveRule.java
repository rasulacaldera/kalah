package com.game.kalah.rules.impl;

import com.game.kalah.constants.BucketType;
import com.game.kalah.constants.NumericConstants;
import com.game.kalah.constants.PlayerId;
import com.game.kalah.dto.BucketDto;
import com.game.kalah.dto.GameDto;
import com.game.kalah.rules.GameRule;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostMoveRule implements GameRule {

    @Override
    public BucketDto apply(GameDto game, BucketDto currentBucket) {

        captureOpponentsStones(game, currentBucket);
        togglePlayer(game, currentBucket);

        return currentBucket;
    }

    private void captureOpponentsStones(GameDto game, BucketDto currentBucket) {
        if (currentBucket.getStoneCount().equals(NumericConstants.INT_ONE) &&
                currentBucket.getType().equals(BucketType.PIT)
                && currentBucket.getOwner().equals(game.getNextPlayer())) {
            BucketDto opposingBucket = getOpposingBucket(game, currentBucket);
            Optional<BucketDto> playerHouse = game.getBuckets()
                    .stream()
                    .filter(bucket -> bucket.getType().equals(BucketType.HOUSE) &&
                            bucket.getOwner().equals(game.getNextPlayer()))
                    .findFirst();
            if (playerHouse.isPresent()) {
                int stoneCount = playerHouse.get().getStoneCount() +
                        opposingBucket.getStoneCount() + currentBucket.getStoneCount();
                playerHouse.get().setStoneCount(stoneCount);
                opposingBucket.setStoneCount(NumericConstants.EMPTY_PIT_STONE_COUNT);
                currentBucket.setStoneCount(NumericConstants.EMPTY_PIT_STONE_COUNT);
            }
        }
    }

    private BucketDto getOpposingBucket(GameDto game, BucketDto currentBucket) {
        List<BucketDto> currentPlayerBuckets = game.getBuckets()
                .stream()
                .filter(bucket -> bucket.getOwner().equals(game.getNextPlayer()))
                .collect(Collectors.toList());

        int playerBucketIndex = currentPlayerBuckets.indexOf(currentBucket);

        List<BucketDto> opponentBuckets = game.getBuckets()
                .stream()
                .filter(bucket -> bucket.getOwner().equals(getOtherPlayer(game.getNextPlayer()))
                        && bucket.getType().equals(BucketType.PIT))
                .sorted(Comparator.comparingInt(BucketDto::getIndex).reversed())
                .collect(Collectors.toList());

        return opponentBuckets.get(playerBucketIndex);
    }

    private void togglePlayer(GameDto game, BucketDto currentBucket) {
        if (currentBucket.getType().equals(BucketType.PIT) ||
                !currentBucket.getOwner().equals(game.getNextPlayer())) {
            game.setNextPlayer(getOtherPlayer(game.getNextPlayer()));
        }
    }

    private PlayerId getOtherPlayer(PlayerId currentPlayer) {
        if (currentPlayer.equals(PlayerId.PLAYER_ONE)) {
            return PlayerId.PLAYER_TWO;
        }
        return PlayerId.PLAYER_ONE;
    }
}
