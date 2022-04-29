import com.game.kalah.constants.BucketType
import com.game.kalah.constants.GameStatus
import com.game.kalah.constants.PlayerIndex
import com.game.kalah.dto.BucketDto
import com.game.kalah.dto.GameDto
import com.game.kalah.dto.PlayerDto
import com.game.kalah.rules.impl.PostMoveRule
import spock.lang.Shared
import spock.lang.Specification

class PostMoveRuleSpec extends Specification {

    @Shared
    private PostMoveRule postMoveRule

    private static final int INITIAL_PIT_STONE_COUNT = 4;
    private static final int INITIAL_HOUSE_STONE_COUNT = 0;

    def setup() {
        postMoveRule = new PostMoveRule()
    }

    def "Apply Rule | Toggles player | Toggles player when it doesn't end in players own House"() {
        given:
        GameDto game = getDummyGame()
        game.nextPlayer = player
        BucketDto currentBucket = game.buckets[endIndex]
        when:
        postMoveRule.apply(game, currentBucket)
        then:
        noExceptionThrown()
        game.nextPlayer != player
        where:
        player                 | endIndex
        PlayerIndex.PLAYER_ONE | 0
        PlayerIndex.PLAYER_ONE | 1
        PlayerIndex.PLAYER_ONE | 2
        PlayerIndex.PLAYER_ONE | 3
        PlayerIndex.PLAYER_ONE | 4
        PlayerIndex.PLAYER_ONE | 5
        PlayerIndex.PLAYER_ONE | 7
        PlayerIndex.PLAYER_ONE | 8
        PlayerIndex.PLAYER_ONE | 9
        PlayerIndex.PLAYER_ONE | 10
        PlayerIndex.PLAYER_ONE | 11
        PlayerIndex.PLAYER_ONE | 12
        PlayerIndex.PLAYER_TWO | 0
        PlayerIndex.PLAYER_TWO | 1
        PlayerIndex.PLAYER_TWO | 2
        PlayerIndex.PLAYER_TWO | 3
        PlayerIndex.PLAYER_TWO | 4
        PlayerIndex.PLAYER_TWO | 5
        PlayerIndex.PLAYER_TWO | 7
        PlayerIndex.PLAYER_TWO | 8
        PlayerIndex.PLAYER_TWO | 9
        PlayerIndex.PLAYER_TWO | 10
        PlayerIndex.PLAYER_TWO | 11
        PlayerIndex.PLAYER_TWO | 12
    }

    def "Apply Rule | Toggles player | Does not toggle when it ends on own player House"() {
        given:
        GameDto game = getDummyGame()
        game.nextPlayer = player
        BucketDto currentBucket = game.buckets[endIndex]
        when:
        postMoveRule.apply(game, currentBucket)
        then:
        noExceptionThrown()
        game.nextPlayer == player
        where:
        player                 | endIndex
        PlayerIndex.PLAYER_ONE | 6
        PlayerIndex.PLAYER_TWO | 13
    }

    def "Apply Rule | Capture Opponent Stones | Does not capture stones when ending on a non empty pit or empty house"() {
        given:
        GameDto game = getDummyGame()
        game.nextPlayer = player
        BucketDto currentBucket = game.buckets[endIndex]
        when:
        postMoveRule.apply(game, currentBucket)
        then:
        noExceptionThrown()
        game.buckets
                .findAll(bucket -> bucket.type == BucketType.HOUSE)
                .every { bucket -> bucket.stoneCount == INITIAL_HOUSE_STONE_COUNT }
        where:
        player                 | endIndex
        PlayerIndex.PLAYER_ONE | 0
        PlayerIndex.PLAYER_ONE | 1
        PlayerIndex.PLAYER_ONE | 2
        PlayerIndex.PLAYER_ONE | 3
        PlayerIndex.PLAYER_ONE | 4
        PlayerIndex.PLAYER_ONE | 5
        PlayerIndex.PLAYER_ONE | 6
        PlayerIndex.PLAYER_TWO | 7
        PlayerIndex.PLAYER_TWO | 8
        PlayerIndex.PLAYER_TWO | 9
        PlayerIndex.PLAYER_TWO | 10
        PlayerIndex.PLAYER_TWO | 11
        PlayerIndex.PLAYER_TWO | 12
        PlayerIndex.PLAYER_TWO | 13
    }

    def "Apply Rule | Capture Opponent Stones | Does not capture stones when ending on a empty opponent empty pit"() {
        given:
        GameDto game = getDummyGame()
        game.nextPlayer = player
        BucketDto currentBucket = game.buckets[endIndex]
        currentBucket.stoneCount = 1
        when:
        postMoveRule.apply(game, currentBucket)
        then:
        noExceptionThrown()
        game.buckets
                .findAll(bucket -> bucket.type == BucketType.HOUSE)
                .every { bucket -> bucket.stoneCount == INITIAL_HOUSE_STONE_COUNT }
        where:
        player                 | endIndex
        PlayerIndex.PLAYER_ONE | 7
        PlayerIndex.PLAYER_ONE | 8
        PlayerIndex.PLAYER_ONE | 9
        PlayerIndex.PLAYER_ONE | 10
        PlayerIndex.PLAYER_ONE | 11
        PlayerIndex.PLAYER_ONE | 12
        PlayerIndex.PLAYER_TWO | 0
        PlayerIndex.PLAYER_TWO | 1
        PlayerIndex.PLAYER_TWO | 2
        PlayerIndex.PLAYER_TWO | 3
        PlayerIndex.PLAYER_TWO | 4
        PlayerIndex.PLAYER_TWO | 5
    }

    def "Apply Rule | Capture Opponent Stones | Captures enemy stones when ending in players empty pit"() {
        given:
        Integer startingPitStoneCount = 1
        Integer emptyPitCount = 0
        GameDto game = getDummyGame()
        game.nextPlayer = player
        BucketDto currentBucket = game.buckets[endIndex]
        currentBucket.stoneCount = startingPitStoneCount
        game.buckets[opponentBucketIndex].stoneCount = INITIAL_PIT_STONE_COUNT
        when:
        postMoveRule.apply(game, currentBucket)
        then:
        noExceptionThrown()
        game.buckets[opponentBucketIndex].stoneCount == emptyPitCount
        game.buckets[endIndex].stoneCount == emptyPitCount
        BucketDto currentPlayerHouse = game.buckets
                .find(bucket -> bucket.type == BucketType.HOUSE && bucket.owner == player)
        currentPlayerHouse.stoneCount == startingPitStoneCount + INITIAL_PIT_STONE_COUNT
        BucketDto opponentPlayerHouse = game.buckets
                .find(bucket -> bucket.type == BucketType.HOUSE && bucket.owner != player)
        opponentPlayerHouse.stoneCount == INITIAL_HOUSE_STONE_COUNT
        where:
        player                 | endIndex | opponentBucketIndex
        PlayerIndex.PLAYER_ONE | 0        | 12
        PlayerIndex.PLAYER_ONE | 1        | 11
        PlayerIndex.PLAYER_ONE | 2        | 10
        PlayerIndex.PLAYER_ONE | 3        | 9
        PlayerIndex.PLAYER_ONE | 4        | 8
        PlayerIndex.PLAYER_ONE | 5        | 7
        PlayerIndex.PLAYER_TWO | 7        | 5
        PlayerIndex.PLAYER_TWO | 8        | 4
        PlayerIndex.PLAYER_TWO | 9        | 3
        PlayerIndex.PLAYER_TWO | 10       | 2
        PlayerIndex.PLAYER_TWO | 11       | 1
        PlayerIndex.PLAYER_TWO | 12       | 0
    }

    private boolean assertBuckets(List<BucketDto> buckets,
                                  Integer startingIndex,
                                  PlayerIndex player,
                                  Integer stoneCount,
                                  boolean validateTotal) {

        if (buckets[startingIndex].stoneCount != 0) {
            return false;
        }

        while (stoneCount > 0) {
            Integer newIndex = ++startingIndex
            if (newIndex >= buckets.size()) {
                newIndex = 0;
            }
            BucketDto newBucket = buckets[newIndex];
            Integer expectedCount = INITIAL_PIT_STONE_COUNT + 1
            if (newBucket.type == BucketType.HOUSE) {

                if (newBucket.owner != player) {
                    continue;
                }

                expectedCount = INITIAL_HOUSE_STONE_COUNT + 1
            }
            boolean stoneAdded = newBucket.stoneCount == expectedCount
            if (!stoneAdded) {
                return false;
            }
            --stoneCount;
        }

        if (validateTotal) {
            boolean isValidTotalStoneCount = buckets.stoneCount.sum() == INITIAL_PIT_STONE_COUNT * 12
            if (!isValidTotalStoneCount) {
                return false
            }
        }

        Integer opponentHouseStones = buckets
                .find { bucket -> bucket.owner != player && bucket.type == BucketType.HOUSE }
                .stoneCount

        return opponentHouseStones == INITIAL_HOUSE_STONE_COUNT;
    }

    private GameDto getDummyGame() {
        return new GameDto(
                gameId: UUID.randomUUID().toString(),
                players: getDummyPlayers(),
                gameStatus: GameStatus.IN_PROGRESS,
                winner: null,
                nextPlayer: PlayerIndex.PLAYER_ONE,
                buckets: [
                        new BucketDto(
                                index: 0,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_ONE,
                                stoneCount: INITIAL_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 1,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_ONE,
                                stoneCount: INITIAL_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 2,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_ONE,
                                stoneCount: INITIAL_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 3,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_ONE,
                                stoneCount: INITIAL_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 4,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_ONE,
                                stoneCount: INITIAL_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 5,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_ONE,
                                stoneCount: INITIAL_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 6,
                                type: BucketType.HOUSE,
                                owner: PlayerIndex.PLAYER_ONE,
                                stoneCount: INITIAL_HOUSE_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 7,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_TWO,
                                stoneCount: INITIAL_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 8,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_TWO,
                                stoneCount: INITIAL_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 9,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_TWO,
                                stoneCount: INITIAL_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 10,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_TWO,
                                stoneCount: INITIAL_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 11,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_TWO,
                                stoneCount: INITIAL_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 12,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_TWO,
                                stoneCount: INITIAL_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 13,
                                type: BucketType.HOUSE,
                                owner: PlayerIndex.PLAYER_TWO,
                                stoneCount: INITIAL_HOUSE_STONE_COUNT
                        )
                ]
        )
    }

    private List<PlayerDto> getDummyPlayers() {

        PlayerDto player1 = new PlayerDto()
        player1.playerIndex = PlayerIndex.PLAYER_ONE
        player1.name = "Dummy1"

        PlayerDto player2 = new PlayerDto()
        player2.playerIndex = PlayerIndex.PLAYER_TWO
        player2.name = "Dummy2"

        return [player1, player2]
    }
}
