package com.game.kalah.dto;

import com.game.kalah.constants.BucketType;
import com.game.kalah.constants.PlayerIndex;
import lombok.Data;

@Data
public class BucketDto {

    private Integer index;
    private BucketType type;
    private PlayerIndex owner;
    private Integer stoneCount;
    private BucketDto nextBucketDto;// todo remove
}
