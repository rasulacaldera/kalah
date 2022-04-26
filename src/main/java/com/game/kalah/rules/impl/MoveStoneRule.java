package com.game.kalah.rules.impl;

import com.game.kalah.constants.BucketType;
import com.game.kalah.constants.PlayerIndex;
import com.game.kalah.dto.BucketDto;
import com.game.kalah.dto.GameDto;
import com.game.kalah.rules.GameRule;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoveStoneRule implements GameRule {

    @Override
    public void apply(GameDto game, BucketDto currentBucket) {

        List<BucketDto> buckets = game.getBuckets();
        int lastIndex = buckets.size() - 1;//todo add numeric constant
        int pebblesToMove = currentBucket.getStoneCount();
        currentBucket.setStoneCount(0); //todo add numeric constant

        while (pebblesToMove > 0) {//todo add numeric constant
            int nextIndex = getNextBucketIndex(currentBucket.getIndex(), lastIndex);
            BucketDto nextBucket = buckets.get(nextIndex);
            if (!isAllowedToAddStone(game.getNextPlayer(), nextBucket)) {
                continue;
            }
            nextBucket.setStoneCount(nextBucket.getStoneCount() + 1);//todo add numeric constant
//            updateNextBucket(currentBucket, nextBucket);
            currentBucket = nextBucket;
            pebblesToMove--;
        }

        togglePlayer(game, currentBucket);
    }

    private void updateNextBucket(BucketDto currentBucket, BucketDto nextBucket) {
        currentBucket.setIndex(nextBucket.getIndex());
        currentBucket.setOwner(nextBucket.getOwner());
        currentBucket.setType(nextBucket.getType());
        currentBucket.setStoneCount(nextBucket.getStoneCount());
    }

    private void togglePlayer(GameDto game, BucketDto currentBucket) {
        if (currentBucket.getType().equals(BucketType.PIT) ||
                !currentBucket.getOwner().equals(game.getNextPlayer())) {
            game.setNextPlayer(getOtherPlayer(game.getNextPlayer()));
        }
    }

    private PlayerIndex getOtherPlayer(PlayerIndex currentPlayer) {
        if (currentPlayer.equals(PlayerIndex.PLAYER_ONE)) {
            return PlayerIndex.PLAYER_TWO;
        }
        return PlayerIndex.PLAYER_ONE;
    }

    private boolean isAllowedToAddStone(PlayerIndex nextPlayer, BucketDto bucket) {

        return bucket.getType().equals(BucketType.PIT) ||
                bucket.getOwner().equals(nextPlayer);
    }

    private Integer getNextBucketIndex(Integer currentIndex, Integer lastIndex) {
        if (currentIndex == lastIndex) return 0;//todo add numeric constant
        else return ++currentIndex;
    }
}
