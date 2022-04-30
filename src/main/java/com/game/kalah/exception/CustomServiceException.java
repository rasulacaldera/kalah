package com.game.kalah.exception;

import com.game.kalah.dto.ApiError;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CustomServiceException extends RuntimeException {
    private final ApiError error;
}
