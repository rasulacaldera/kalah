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
    public void apply(GameDto game, Integer pitIndex) {

        List<BucketDto> buckets = game.getBuckets();
        int lastIndex = buckets.size() - 1;//todo add numeric constant
        BucketDto bucketDto = buckets.get(pitIndex);
        int pebblesToMove = bucketDto.getStoneCount();
        bucketDto.setStoneCount(0); //todo add numeric constant

        while (pebblesToMove > 0) {//todo add numeric constant
            int nextIndex = getNextBucketIndex(pitIndex, lastIndex);
            BucketDto bucket = buckets.get(nextIndex);
            if (!isAllowedToAddStone(game.getNextPlayer(), bucket)) {
                continue;
            }
            bucket.setStoneCount(bucket.getStoneCount() + 1);//todo add numeric constant
            pitIndex = nextIndex;
            pebblesToMove--;
        }

        togglePlayer(game, pitIndex);

    }

    private void togglePlayer(GameDto game, int pitIndex) {
        BucketDto bucketDto = game.getBuckets().get(pitIndex);
        if (bucketDto.getType().equals(BucketType.PIT) ||
                !bucketDto.getOwner().equals(game.getNextPlayer())) {
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
