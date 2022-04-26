package com.game.kalah.dto;

import com.game.kalah.constants.ErrorMessage;
import lombok.Getter;

@Getter
public class ApiError {

    private final String code;
    private final String message;

    public ApiError(ErrorMessage errorMessage, String... args) {
        this.code = errorMessage.getCode();
        this.message = String.format(errorMessage.getMessage(), args);
    }

}
