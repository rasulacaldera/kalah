package com.game.kalah.dto;

import com.game.kalah.constants.BucketType;
import com.game.kalah.constants.PlayerId;
import lombok.Data;

@Data
public class BucketDto {

    private Integer index;
    private BucketType type;
    private PlayerId owner;
    private Integer stoneCount;
}
