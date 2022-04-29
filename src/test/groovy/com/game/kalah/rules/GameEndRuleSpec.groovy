import com.game.kalah.constants.BucketType
import com.game.kalah.constants.GameStatus
import com.game.kalah.constants.PlayerIndex
import com.game.kalah.dto.BucketDto
import com.game.kalah.dto.GameDto
import com.game.kalah.dto.PlayerDto
import com.game.kalah.rules.impl.GameEndRule
import spock.lang.Shared
import spock.lang.Specification

class GameEndRuleSpec extends Specification {

    @Shared
    private GameEndRule gameEndRule

    private static final int INITIAL_EMPTY_PIT_STONE_COUNT = 0;
    private static final int INITIAL_HOUSE_STONE_COUNT = 0;

    def setup() {
        gameEndRule = new GameEndRule()
    }

    def "Apply Rule | Does not pick a winner when game is not over"() {
        given:
        GameDto game = getDummyGame()
        BucketDto currentBucket = game.buckets[player1NonEmptyPit]
        game.buckets[player1NonEmptyPit].stoneCount = 1
        game.buckets[player2NonEmptyPit].stoneCount = 1
        when:
        gameEndRule.apply(game, currentBucket)
        then:
        noExceptionThrown()
        game.winner == null
        where:
        player1NonEmptyPit | player2NonEmptyPit
        0                  | 7
        1                  | 8
        2                  | 9
        3                  | 10
        4                  | 11
        5                  | 12
    }

    def "Apply Rule | Picks correct winner when game is over"() {
        given:
        GameDto game = getDummyGame()
        BucketDto currentBucket = game.buckets[nonEmptyPit]
        currentBucket.stoneCount = 1
        game.buckets[6].stoneCount = player1HouseStones
        game.buckets[13].stoneCount = player2HouseStones
        when:
        gameEndRule.apply(game, currentBucket)
        then:
        noExceptionThrown()
        game.winner.playerIndex == winner
        where:
        nonEmptyPit | player1HouseStones | player2HouseStones | winner
        1           | 10                 | 20                 | PlayerIndex.PLAYER_TWO
        2           | 22                 | 20                 | PlayerIndex.PLAYER_ONE
        3           | 4                  | 15                 | PlayerIndex.PLAYER_TWO
        4           | 22                 | 20                 | PlayerIndex.PLAYER_ONE
        5           | 12                 | 21                 | PlayerIndex.PLAYER_TWO
        7           | 30                 | 1                  | PlayerIndex.PLAYER_ONE
        8           | 00                 | 40                 | PlayerIndex.PLAYER_TWO
        9           | 12                 | 19                 | PlayerIndex.PLAYER_TWO
        10          | 12                 | 13                 | PlayerIndex.PLAYER_TWO
        11          | 13                 | 12                 | PlayerIndex.PLAYER_ONE
        12          | 30                 | 1                  | PlayerIndex.PLAYER_ONE
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
                                stoneCount: INITIAL_EMPTY_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 1,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_ONE,
                                stoneCount: INITIAL_EMPTY_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 2,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_ONE,
                                stoneCount: INITIAL_EMPTY_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 3,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_ONE,
                                stoneCount: INITIAL_EMPTY_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 4,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_ONE,
                                stoneCount: INITIAL_EMPTY_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 5,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_ONE,
                                stoneCount: INITIAL_EMPTY_PIT_STONE_COUNT
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
                                stoneCount: INITIAL_EMPTY_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 8,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_TWO,
                                stoneCount: INITIAL_EMPTY_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 9,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_TWO,
                                stoneCount: INITIAL_EMPTY_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 10,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_TWO,
                                stoneCount: INITIAL_EMPTY_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 11,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_TWO,
                                stoneCount: INITIAL_EMPTY_PIT_STONE_COUNT
                        ),
                        new BucketDto(
                                index: 12,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_TWO,
                                stoneCount: INITIAL_EMPTY_PIT_STONE_COUNT
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
