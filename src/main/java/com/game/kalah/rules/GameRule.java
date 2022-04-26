package com.game.kalah.rules;

import com.game.kalah.dto.BucketDto;
import com.game.kalah.dto.GameDto;

public interface GameRule {

    BucketDto apply(GameDto game, BucketDto currentBucket);
}
