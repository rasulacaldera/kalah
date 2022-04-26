package com.game.kalah.service.impl;

import com.game.kalah.constants.BucketType;
import com.game.kalah.constants.PlayerIndex;
import com.game.kalah.dto.BucketDto;
import com.game.kalah.dto.CreateGameRequestModel;
import com.game.kalah.dto.GameDto;
import com.game.kalah.dto.PlayerDto;
import com.game.kalah.repository.GameRepository;
import com.game.kalah.service.GameService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {

    private static final int INITIAL_STONE_COUNT_PER_BUCKET = 4;

    final GameRepository gameRepository;

    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public GameDto createGame(CreateGameRequestModel gameRequest) {

        GameDto game = new GameDto();
        game.setGameId(UUID.randomUUID().toString());
        game.setPlayers(initializeAndGetPlayers(gameRequest));
        game.setBuckets(getDefaultBuckets());

        gameRepository.save(game);

        return game;
    }

    private List<PlayerDto> initializeAndGetPlayers(CreateGameRequestModel gameRequest) {

        List<PlayerDto> players = new ArrayList<>();
        PlayerDto player1 = new PlayerDto(PlayerIndex.PLAYER_ONE, gameRequest.getPlayerOneName());
        PlayerDto player2 = new PlayerDto(PlayerIndex.PLAYER_TWO, gameRequest.getPlayerTwoName());

        players.add(player1);
        players.add(player2);

        return players;
    }

    private List<BucketDto> getDefaultBuckets() {
        List<BucketDto> buckets = getDefaultBucketsForPlayer(PlayerIndex.PLAYER_ONE);
        buckets.addAll(getDefaultBucketsForPlayer(PlayerIndex.PLAYER_TWO));

        return processAndLinkBuckets(buckets);
    }

    private List<BucketDto> getDefaultBucketsForPlayer(PlayerIndex playerIndex) {
        List<BucketDto> buckets = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            BucketDto bucket = new BucketDto();
            bucket.setOwner(playerIndex);
            bucket.setStoneCount(INITIAL_STONE_COUNT_PER_BUCKET);
            bucket.setType(BucketType.PIT);
            buckets.add(bucket);
        }

        BucketDto house = new BucketDto();
        house.setOwner(playerIndex);
        house.setStoneCount(0);
        house.setType(BucketType.HOUSE);
        buckets.add(house);

        return buckets;

    }

    private List<BucketDto> processAndLinkBuckets(List<BucketDto> buckets) {
//        Integer lastIndex = buckets.size() - 1;
        for (int i = 0; i < buckets.size(); i++) {
            BucketDto bucket = buckets.get(i);
            bucket.setIndex(i);
//            bucket.setNextBucketDto(buckets.get(getNextLinkIndex(i, lastIndex)));
        }

        return buckets;
    }

//    private Integer getNextLinkIndex(Integer currentIndex, Integer lastIndex) { //todo remove
//        if (currentIndex == lastIndex) return 0;
//        else return ++currentIndex;
//    }
}
