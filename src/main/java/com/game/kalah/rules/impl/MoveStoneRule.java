package com.game.kalah.rules.impl;

import com.game.kalah.constants.BucketType;
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
        int lastIndex = buckets.size() - 1;//todo add numeric constant
        int pebblesToMove = currentBucket.getStoneCount();
        currentBucket.setStoneCount(0); //todo add numeric constant

        while (pebblesToMove > 0) {//todo add numeric constant
            int nextIndex = getNextBucketIndex(currentBucket.getIndex(), lastIndex);
            currentBucket = buckets.get(nextIndex);
            if (!isAllowedToAddStone(game.getNextPlayer(), currentBucket)) {
                continue;
            }
            currentBucket.setStoneCount(currentBucket.getStoneCount() + 1);//todo add numeric constant
            pebblesToMove--;
        }

        return currentBucket;
    }

    private boolean isAllowedToAddStone(PlayerId nextPlayer, BucketDto bucket) {

        return bucket.getType().equals(BucketType.PIT) ||
                bucket.getOwner().equals(nextPlayer);
    }

    private Integer getNextBucketIndex(Integer currentIndex, Integer lastIndex) {
        if (currentIndex.equals(lastIndex)) return 0;//todo add numeric constant
        else return ++currentIndex;
    }
}
