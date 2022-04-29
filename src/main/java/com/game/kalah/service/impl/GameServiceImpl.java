package com.game.kalah.service.impl;

import com.game.kalah.constants.BucketType;
import com.game.kalah.constants.ErrorMessage;
import com.game.kalah.constants.GameStatus;
import com.game.kalah.constants.PlayerId;
import com.game.kalah.dto.*;
import com.game.kalah.exception.CustomServiceException;
import com.game.kalah.repository.GameRepository;
import com.game.kalah.rules.GameRule;
import com.game.kalah.rules.impl.GameEndRule;
import com.game.kalah.rules.impl.MoveStoneRule;
import com.game.kalah.rules.impl.PostMoveRule;
import com.game.kalah.rules.impl.PreMoveRule;
import com.game.kalah.service.GameService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {

    private static final int INITIAL_STONE_COUNT_PER_BUCKET = 4;
    private static final int MAX_BUCKET_COUNT = 14;

    final GameRepository gameRepository;
    final MoveStoneRule moveStoneRule;
    final PostMoveRule postMoveRule;
    final PreMoveRule preMoveRule;
    final GameEndRule gameEndRule;

    public GameServiceImpl(GameRepository gameRepository, MoveStoneRule moveStoneRule,
                           PostMoveRule postMoveRule, PreMoveRule preMoveRule, GameEndRule gameEndRule) {
        this.gameRepository = gameRepository;
        this.moveStoneRule = moveStoneRule;
        this.postMoveRule = postMoveRule;
        this.preMoveRule = preMoveRule;
        this.gameEndRule = gameEndRule;
    }

    @Override
    public GameDto createGame(CreateGameRequestModel gameRequest) {

        GameDto game = new GameDto();
        game.setGameId(UUID.randomUUID().toString());
        game.setPlayers(initializeAndGetPlayers(gameRequest));
        game.setBuckets(getDefaultBuckets());
        game.setNextPlayer(PlayerId.PLAYER_ONE);
        game.setGameStatus(GameStatus.IN_PROGRESS);

        gameRepository.save(game);

        return game;
    }

    @Override
    public GameDto makeMove(String gameId, Integer pitIndex) {

        validateMoveInputs(gameId, pitIndex);

        GameDto game = gameRepository.getById(gameId);
        BucketDto currentBucket = game.getBuckets().get(pitIndex);

        List<GameRule> rules = getGameRules();

        if (rules.isEmpty()) {
            throw CustomServiceException
                    .builder()
                    .error(new ApiError(ErrorMessage.RULES_DOESNT_EXIST, gameId))
                    .build();
        }

        for (GameRule rule : rules) {
            currentBucket = rule.apply(game, currentBucket);
        }

        gameRepository.save(game);

        return game;
    }

    private void validateMoveInputs(String gameId, Integer pitIndex) {

        if (gameId == null || pitIndex == null) {
            throw CustomServiceException
                    .builder()
                    .error(new ApiError(ErrorMessage.GAME_ID_OR_PIT_INDEX_NOT_FOUND))
                    .build();
        }

        if (pitIndex < 0 || pitIndex >= MAX_BUCKET_COUNT) { //todo use numeric constant
            throw CustomServiceException
                    .builder()
                    .error(new ApiError(ErrorMessage.PIT_INDEX_OUT_OF_BOUNDS,
                            String.valueOf(pitIndex)))
                    .build();
        }

        if (!gameRepository.gameExists(gameId)) {
            throw CustomServiceException
                    .builder()
                    .error(new ApiError(ErrorMessage.GAME_NOT_FOUND, gameId))
                    .build();
        }
    }

    private List<PlayerDto> initializeAndGetPlayers(CreateGameRequestModel gameRequest) {

        List<PlayerDto> players = new ArrayList<>();
        PlayerDto player1 = new PlayerDto(PlayerId.PLAYER_ONE, gameRequest.getPlayerOneName());
        PlayerDto player2 = new PlayerDto(PlayerId.PLAYER_TWO, gameRequest.getPlayerTwoName());

        players.add(player1);
        players.add(player2);

        return players;
    }

    private List<BucketDto> getDefaultBuckets() {
        List<BucketDto> buckets = getDefaultBucketsForPlayer(PlayerId.PLAYER_ONE);
        buckets.addAll(getDefaultBucketsForPlayer(PlayerId.PLAYER_TWO));

        return processAndLinkBuckets(buckets);
    }

    private List<BucketDto> getDefaultBucketsForPlayer(PlayerId playerId) {
        List<BucketDto> buckets = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            BucketDto bucket = new BucketDto();
            bucket.setOwner(playerId);
            bucket.setStoneCount(INITIAL_STONE_COUNT_PER_BUCKET);
            bucket.setType(BucketType.PIT);
            buckets.add(bucket);
        }

        BucketDto house = new BucketDto();
        house.setOwner(playerId);
        house.setStoneCount(0);
        house.setType(BucketType.HOUSE);
        buckets.add(house);

        return buckets;

    }

    private List<BucketDto> processAndLinkBuckets(List<BucketDto> buckets) {
        for (int i = 0; i < buckets.size(); i++) {
            BucketDto bucket = buckets.get(i);
            bucket.setIndex(i);
        }

        return buckets;
    }

    private List<GameRule> getGameRules() {
        List<GameRule> rules = new ArrayList<>();
        rules.add(preMoveRule);
        rules.add(moveStoneRule);
        rules.add(postMoveRule);
        rules.add(gameEndRule);

        return rules;
    }
}
