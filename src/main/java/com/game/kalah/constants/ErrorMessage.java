package com.game.kalah.constants;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    UNHANDLED_EXCEPTION("KAL_0001", "Unhandled Exception"),
    GAME_NOT_FOUND("KAL_0002", "Cannot with game with ID: %s"),
    RULES_DOESNT_EXIST("KAL_0003", "No rules exist for Game"),
    GAME_ID_OR_PIT_INDEX_NOT_FOUND("KAL_0004", "Invalid Request. Should define Game ID and PitIndex"),
    PIT_INDEX_OUT_OF_BOUNDS("KAL_0005", "Cannot with game with ID: %s"),
    WRONG_PLAYER_TURN("KAL_0006", "Wrong Turn! It is player %s's turn"),
    CANNOT_START_FROM_HOUSE("KAL_0007", "Cannot start from a House"),
    PIT_HAS_NO_STONES("KAL_0008", "Pit does not contain any stones");

    private final String code;
    private final String message;

    ErrorMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
