import com.game.kalah.constants.BucketType
import com.game.kalah.constants.ErrorMessage
import com.game.kalah.constants.GameStatus
import com.game.kalah.constants.PlayerIndex
import com.game.kalah.dto.BucketDto
import com.game.kalah.dto.CreateGameRequestModel
import com.game.kalah.dto.GameDto
import com.game.kalah.dto.PlayerDto
import com.game.kalah.exception.CustomServiceException
import com.game.kalah.repository.GameRepository
import com.game.kalah.rules.impl.GameEndRule
import com.game.kalah.rules.impl.MoveStoneRule
import com.game.kalah.rules.impl.PostMoveRule
import com.game.kalah.rules.impl.PreMoveRule
import com.game.kalah.service.GameService
import com.game.kalah.service.impl.GameServiceImpl
import spock.lang.Shared
import spock.lang.Specification

class GameServiceImplSpecification extends Specification {

    @Shared
    private GameService gameService
    private GameRepository gameRepository = Mock(GameRepository)
    private MoveStoneRule moveStoneRule = Mock(MoveStoneRule)
    private PostMoveRule postMoveRule = Mock(PostMoveRule)
    private PreMoveRule preMoveRule = Mock(PreMoveRule)
    private GameEndRule gameEndRule = Mock(GameEndRule)

    private static final int INITIAL_STONE_COUNT = 4;
    private static final int INITIAL_HOUSE_STONE_COUNT = 0;

    def setup() {
        gameService = new GameServiceImpl(gameRepository, moveStoneRule, postMoveRule, preMoveRule, gameEndRule)
    }

    def "Create Game | Successfully creates a game with valid inputs"() {
        given:
        PlayerIndex player1 = PlayerIndex.PLAYER_ONE
        PlayerIndex player2 = PlayerIndex.PLAYER_TWO
        CreateGameRequestModel createGameRequest = new CreateGameRequestModel(
                playerOneName: "Player1",
                playerTwoName: "Player2"
        )
        when:
        GameDto game = gameService.createGame(createGameRequest)
        then:
        noExceptionThrown()
        game.gameId != null
        game.nextPlayer == player1
        game.gameStatus == GameStatus.IN_PROGRESS
        game.winner == null
        game.buckets.size() == 14
        game.buckets.findAll { it -> it.owner == player1 && it.type == BucketType.PIT }.size() == 6
        game.buckets.findAll { it -> it.owner == player2 && it.type == BucketType.PIT }.size() == 6
        game.buckets.findAll { it -> it.owner == player1 && it.type == BucketType.HOUSE }.size() == 1
        game.buckets.findAll { it -> it.owner == player2 && it.type == BucketType.HOUSE }.size() == 1
        game.buckets.findAll { it -> it.type == BucketType.PIT }
                .every { it -> it.stoneCount == INITIAL_STONE_COUNT }
        game.buckets.findAll { it -> it.type == BucketType.HOUSE }
                .every { it -> it.stoneCount == INITIAL_HOUSE_STONE_COUNT }
    }

    def "Make Move | Validations | Throws an Exception when either input is null"() {
        when:
        gameService.makeMove(gameId, pit)
        then:
        Exception ex = thrown(CustomServiceException.class)
        ex.error.code == ErrorMessage.GAME_ID_OR_PIT_INDEX_NOT_FOUND.code
        ex.error.message == ErrorMessage.GAME_ID_OR_PIT_INDEX_NOT_FOUND.message
        where:
        gameId | pit
        null   | null
        "id"   | null
        null   | 1
    }

    def "Make Move | Validations | Throws an Exception when Pit is invalid"() {
        when:
        gameService.makeMove("game-id", pitIndex)
        then:
        Exception ex = thrown(CustomServiceException.class)
        ex.error.code == ErrorMessage.PIT_INDEX_OUT_OF_BOUNDS.code
        ex.error.message == String.format(ErrorMessage.PIT_INDEX_OUT_OF_BOUNDS.message, pitIndex)
        where:
        pitIndex << [-1, -100, 14, 50, 100]

    }

    def "Make Move | Validations | Throws an Exception when game does not exist"() {
        given:
        String gameId = "invalid-game-id"
        gameRepository.gameExists(gameId) >> false
        when:
        gameService.makeMove(gameId, 1)
        then:
        Exception ex = thrown(CustomServiceException.class)
        ex.error.code == ErrorMessage.GAME_NOT_FOUND.code
        ex.error.message == String.format(ErrorMessage.GAME_NOT_FOUND.message, gameId)
    }

    def "Make Move | Calls all game rules in order"() { //todo mock and return
        given:
        GameDto game = getDummyGame();
        String gameId = game.gameId
        gameRepository.gameExists(gameId) >> true
        gameRepository.getById(gameId) >> game
        1 * preMoveRule.apply(game, _)
        1 * moveStoneRule.apply(game, _)
        1 * postMoveRule.apply(game, _)
        1 * gameEndRule.apply(game, _)
        when:
        gameService.makeMove(gameId, 1)
        then:
        noExceptionThrown()
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
