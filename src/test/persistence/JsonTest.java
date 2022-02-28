package persistence;

import model.Board;
import model.GameToken;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkBoard(String playerTurn,  Board board, String name, GameToken[][] gameBoard) {
        assertEquals(playerTurn, board.getPlayerTurn());
        assertEquals(gameBoard, board.getGameBoard());
        assertEquals(name, board.getBoardName());
    }
}
