import com.game.kalah.constants.BucketType
import com.game.kalah.constants.GameStatus
import com.game.kalah.constants.PlayerId
import com.game.kalah.dto.BucketDto
import com.game.kalah.dto.GameDto
import com.game.kalah.dto.PlayerDto
import com.game.kalah.rules.impl.MoveStoneRule
import spock.lang.Shared
import spock.lang.Specification

class MoveStoneRuleSpec extends Specification {

    @Shared
    private MoveStoneRule moveStoneRule

    private static final int INITIAL_PIT_STONE_COUNT = 4;
    private static final int INITIAL_HOUSE_STONE_COUNT = 0;

    def setup() {
        moveStoneRule = new MoveStoneRule()
    }

    def "Apply Rule | Moves stones"() {
        given:
        GameDto game = getDummyGame()
        game.nextPlayer = player
        BucketDto currentBucket = game.buckets[pitIndex]
        Integer stoneCount = currentBucket.stoneCount
        when:
        moveStoneRule.apply(game, currentBucket)
        then:
        noExceptionThrown()
        assertBuckets(game.buckets, pitIndex, player, stoneCount, true)
        where:
        player              | pitIndex
        PlayerId.PLAYER_ONE | 0
        PlayerId.PLAYER_ONE | 1
        PlayerId.PLAYER_ONE | 2
        PlayerId.PLAYER_ONE | 3
        PlayerId.PLAYER_ONE | 4
        PlayerId.PLAYER_ONE | 5
        PlayerId.PLAYER_TWO | 7
        PlayerId.PLAYER_TWO | 8
        PlayerId.PLAYER_TWO | 9
        PlayerId.PLAYER_TWO | 10
        PlayerId.PLAYER_TWO | 11
        PlayerId.PLAYER_TWO | 12
    }

    def "Apply Rule | Moves stones | Skips opponents House"() {
        given:
        Integer stoneCount = 12
        GameDto game = getDummyGame()
        game.nextPlayer = player
        BucketDto currentBucket = game.buckets[pitIndex]
        currentBucket.stoneCount = stoneCount
        when:
        moveStoneRule.apply(game, currentBucket)
        then:
        noExceptionThrown()
        assertBuckets(game.buckets, pitIndex, player, stoneCount, false)
        where:
        player              | pitIndex
        PlayerId.PLAYER_ONE | 0
        PlayerId.PLAYER_ONE | 1
        PlayerId.PLAYER_ONE | 2
        PlayerId.PLAYER_ONE | 3
        PlayerId.PLAYER_ONE | 4
        PlayerId.PLAYER_ONE | 5
        PlayerId.PLAYER_TWO | 7
        PlayerId.PLAYER_TWO | 8
        PlayerId.PLAYER_TWO | 9
        PlayerId.PLAYER_TWO | 10
        PlayerId.PLAYER_TWO | 11
        PlayerId.PLAYER_TWO | 12
    }

    private boolean assertBuckets(List<BucketDto> buckets,
                                  Integer startingIndex,
                                  PlayerId player,
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
                nextPlayer: PlayerId.PLAYER_ONE,
                buckets: [
                        new BucketDto(
                                index: 0,
                                type: BucketType.PIT,
                                owner: PlayerId.PLAYER_ONE,
                                stoneCount: INITIAL_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 1,
                                type: BucketType.PIT,
                                owner: PlayerId.PLAYER_ONE,
                                stoneCount: INITIAL_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 2,
                                type: BucketType.PIT,
                                owner: PlayerId.PLAYER_ONE,
                                stoneCount: INITIAL_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 3,
                                type: BucketType.PIT,
                                owner: PlayerId.PLAYER_ONE,
                                stoneCount: INITIAL_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 4,
                                type: BucketType.PIT,
                                owner: PlayerId.PLAYER_ONE,
                                stoneCount: INITIAL_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 5,
                                type: BucketType.PIT,
                                owner: PlayerId.PLAYER_ONE,
                                stoneCount: INITIAL_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 6,
                                type: BucketType.HOUSE,
                                owner: PlayerId.PLAYER_ONE,
                                stoneCount: INITIAL_HOUSE_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 7,
                                type: BucketType.PIT,
                                owner: PlayerId.PLAYER_TWO,
                                stoneCount: INITIAL_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 8,
                                type: BucketType.PIT,
                                owner: PlayerId.PLAYER_TWO,
                                stoneCount: INITIAL_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 9,
                                type: BucketType.PIT,
                                owner: PlayerId.PLAYER_TWO,
                                stoneCount: INITIAL_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 10,
                                type: BucketType.PIT,
                                owner: PlayerId.PLAYER_TWO,
                                stoneCount: INITIAL_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 11,
                                type: BucketType.PIT,
                                owner: PlayerId.PLAYER_TWO,
                                stoneCount: INITIAL_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 12,
                                type: BucketType.PIT,
                                owner: PlayerId.PLAYER_TWO,
                                stoneCount: INITIAL_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 13,
                                type: BucketType.HOUSE,
                                owner: PlayerId.PLAYER_TWO,
                                stoneCount: INITIAL_HOUSE_STONE_COUNT
                        )
                ]
        )
    }

    private List<PlayerDto> getDummyPlayers() {

        PlayerDto player1 = new PlayerDto()
        player1.playerId = PlayerId.PLAYER_ONE
        player1.name = "Dummy1"

        PlayerDto player2 = new PlayerDto()
        player2.playerId = PlayerId.PLAYER_TWO
        player2.name = "Dummy2"

        return [player1, player2]
    }
}
