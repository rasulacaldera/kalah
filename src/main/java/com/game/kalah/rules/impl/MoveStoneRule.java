package com.game.kalah.rules.impl;

import com.game.kalah.constants.BucketType;
import com.game.kalah.constants.NumericConstants;
import com.game.kalah.constants.PlayerId;
import com.game.kalah.dto.BucketDto;
import com.game.kalah.dto.GameDto;
import com.game.kalah.rules.GameRule;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoveStoneRule implements GameRule {

    @Override
    public BucketDto apply(GameDto game, BucketDto currentBucket) {

        List<BucketDto> buckets = game.getBuckets();
        int lastIndex = buckets.size() - NumericConstants.INT_ONE;
        int pebblesToMove = currentBucket.getStoneCount();
        currentBucket.setStoneCount(NumericConstants.EMPTY_PIT_STONE_COUNT);

        while (pebblesToMove > NumericConstants.EMPTY_PIT_STONE_COUNT) {
            int nextIndex = getNextBucketIndex(currentBucket.getIndex(), lastIndex);
            currentBucket = buckets.get(nextIndex);
            if (!isAllowedToAddStone(game.getNextPlayer(), currentBucket)) {
                continue;
            }
            currentBucket.setStoneCount(currentBucket.getStoneCount() + NumericConstants.INT_ONE);
            pebblesToMove--;
        }

        return currentBucket;
    }

    private boolean isAllowedToAddStone(PlayerId nextPlayer, BucketDto bucket) {

        return bucket.getType().equals(BucketType.PIT) ||
                bucket.getOwner().equals(nextPlayer);
    }

    private Integer getNextBucketIndex(Integer currentIndex, Integer lastIndex) {
        if (currentIndex.equals(lastIndex)) return NumericConstants.INT_ZERO;
        else return ++currentIndex;
    }
}
