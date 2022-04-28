import com.game.kalah.constants.BucketType
import com.game.kalah.constants.ErrorMessage
import com.game.kalah.constants.GameStatus
import com.game.kalah.constants.PlayerIndex
import com.game.kalah.dto.BucketDto
import com.game.kalah.dto.GameDto
import com.game.kalah.dto.PlayerDto
import com.game.kalah.exception.CustomServiceException
import com.game.kalah.rules.impl.PreMoveRule
import spock.lang.Shared
import spock.lang.Specification

class PreMoveRuleSpec extends Specification {

    @Shared
    private PreMoveRule preMoveRule

    def setup() {
        preMoveRule = new PreMoveRule()
    }

    def "Apply Rule | Throws an Exception when the Game has already ended"() {
        given:
        GameDto game = getDummyGame()
        game.gameStatus = GameStatus.FINISHED
        BucketDto currentBucket = game.buckets[0]
        when:
        preMoveRule.apply(game, currentBucket)
        then:
        Exception ex = thrown(CustomServiceException.class)
        ex.error.code == ErrorMessage.GAME_ALREADY_FINISHED.code
        ex.error.message == ErrorMessage.GAME_ALREADY_FINISHED.message
    }

    def "Apply Rule | Throws an Exception when the wrong player makes a move"() {
        given:
        String player1Name = "Test Player"
        GameDto game = getDummyGame()
        game.players[0].name = player1Name
        BucketDto currentBucket = game.buckets[pitIndex]
        when:
        preMoveRule.apply(game, currentBucket)
        then:
        Exception ex = thrown(CustomServiceException.class)
        ex.error.code == ErrorMessage.WRONG_PLAYER_TURN.code
        ex.error.message == String.format(ErrorMessage.WRONG_PLAYER_TURN.message, player1Name)
        where:
        pitIndex << [8, 9, 10, 11, 12, 13]
    }

    def "Apply Rule | Throws an Exception when player picks a house"() {
        given:
        GameDto game = getDummyGame()
        game.nextPlayer = player
        BucketDto currentBucket = game.buckets[pitIndex]
        when:
        preMoveRule.apply(game, currentBucket)
        then:
        Exception ex = thrown(CustomServiceException.class)
        ex.error.code == ErrorMessage.CANNOT_START_FROM_HOUSE.code
        ex.error.message == ErrorMessage.CANNOT_START_FROM_HOUSE.message
        where:
        player                 | pitIndex
        PlayerIndex.PLAYER_ONE | 6
        PlayerIndex.PLAYER_TWO | 13
    }

    def "Apply Rule | Throws an Exception when Pit has no stones"() {
        given:
        GameDto game = getDummyGame()
        game.nextPlayer = player
        BucketDto currentBucket = game.buckets[pitIndex]
        currentBucket.stoneCount = 0
        when:
        preMoveRule.apply(game, currentBucket)
        then:
        Exception ex = thrown(CustomServiceException.class)
        ex.error.code == ErrorMessage.PIT_HAS_NO_STONES.code
        ex.error.message == ErrorMessage.PIT_HAS_NO_STONES.message
        where:
        player                 | pitIndex
        PlayerIndex.PLAYER_ONE | 0
        PlayerIndex.PLAYER_ONE | 3
        PlayerIndex.PLAYER_ONE | 5
        PlayerIndex.PLAYER_TWO | 7
        PlayerIndex.PLAYER_TWO | 10
        PlayerIndex.PLAYER_TWO | 12
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
                                stoneCount: 4
                        ),
                        new BucketDto(
                                index: 1,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_ONE,
                                stoneCount: 4
                        ),
                        new BucketDto(
                                index: 2,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_ONE,
                                stoneCount: 4
                        ),
                        new BucketDto(
                                index: 3,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_ONE,
                                stoneCount: 4
                        ),
                        new BucketDto(
                                index: 4,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_ONE,
                                stoneCount: 4
                        ),
                        new BucketDto(
                                index: 5,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_ONE,
                                stoneCount: 4
                        ),
                        new BucketDto(
                                index: 6,
                                type: BucketType.HOUSE,
                                owner: PlayerIndex.PLAYER_ONE,
                                stoneCount: 0
                        ),
                        new BucketDto(
                                index: 7,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_TWO,
                                stoneCount: 4
                        ),
                        new BucketDto(
                                index: 8,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_TWO,
                                stoneCount: 4
                        ),
                        new BucketDto(
                                index: 9,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_TWO,
                                stoneCount: 4
                        ),
                        new BucketDto(
                                index: 10,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_TWO,
                                stoneCount: 4
                        ),
                        new BucketDto(
                                index: 11,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_TWO,
                                stoneCount: 4
                        ),
                        new BucketDto(
                                index: 12,
                                type: BucketType.PIT,
                                owner: PlayerIndex.PLAYER_TWO,
                                stoneCount: 4
                        ),
                        new BucketDto(
                                index: 13,
                                type: BucketType.HOUSE,
                                owner: PlayerIndex.PLAYER_TWO,
                                stoneCount: 0
                        )
                ]
        )
    }

    List<PlayerDto> getDummyPlayers() {

        PlayerDto player1 = new PlayerDto()
        player1.playerIndex = PlayerIndex.PLAYER_ONE
        player1.name = "Dummy1"

        PlayerDto player2 = new PlayerDto()
        player2.playerIndex = PlayerIndex.PLAYER_TWO
        player2.name = "Dummy2"

        return [player1, player2]
    }
}
