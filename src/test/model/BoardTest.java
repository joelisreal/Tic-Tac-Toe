package model;

import exceptions.BoardPositionOccupiedException;
import exceptions.WrongInputAtPlayMenuException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

// Tests for Board Class
public class BoardTest {

    private Board board;
    private TokenOne testX;
    private TokenTwo testO;
    private Scanner input;

    @BeforeEach
    public void setUp() {
        board = new Board();
        testX = new TokenOne();
        testO = new TokenTwo();
        //board.gameBoard = new ArrayList<ArrayList<GameToken>>();
        board.setPlayerTurn("X");
        input = new Scanner(System.in);
    }

    @Test
    public void testAddXs() {
        try {
            board.addXs(0, 0);
            // assertEquals(new Xs().getToken(), testX.getToken());
            assertEquals(board.getTokenFromArr(0, 0), testX.getToken());
            board.addXs(1, 0);
            assertEquals(board.getTokenFromArr(1, 0), testX.getToken());
            board.addXs(2, 1);
            assertEquals(board.getTokenFromArr(2, 1), testX.getToken());

            board.addXs(2, 1);
            assertEquals(board.getTokenFromArr(2, 1), testX.getToken());

            assertFalse(board.getTokenFromArr(2, 1).equals(testO.getToken()));

            board.addOs(0, 0);
            fail("Should have thrown exception");
        } catch (BoardPositionOccupiedException e) {
            System.out.println("Position occupied. Please select another.");
        }
    }

    @Test
    public void testAddOs() {
        try {
            //board.gameBoard[0][0] = new TokenOne();
            board.addOs(0, 0);
            assertEquals(board.getTokenFromArr(0, 0), testO.getToken());
            board.addOs(2, 2);
            assertEquals(board.getTokenFromArr(2, 2), testO.getToken());
            board.addOs(1, 2);
            assertEquals(board.getTokenFromArr(1, 2), testO.getToken());

            board.addOs(1, 2);
            assertFalse(board.getTokenFromArr(1, 2).equals(testX.getToken()));

            board.addXs(0, 0);
            //assertFalse(board.getTokenFromArr(0, 0).equals(testX.getToken()));
            fail("Should have thrown exception");
        } catch (BoardPositionOccupiedException e) {
            System.out.println("Position occupied. Please select another.");
        }
    }

    @Test
    public void testWrongInput() {
        try {
            System.out.println("Select from:");
            System.out.println("\tb to go back to the display menu");
            System.out.println("\tc to continue");
            //String choose = input.nextLine();
            String choose = "5";
            if (!choose.equals("b") && !choose.equals("c")) {
                throw new WrongInputAtPlayMenuException();
            }
        } catch (WrongInputAtPlayMenuException w) {
            System.out.println("Invalid input. Please select either b or c");
        }
    }

    @Test
    public void testNextTurn() {
        board.nextTurn();
        assertEquals(board.getPlayerTurn(), "O");
        board.nextTurn();
        assertEquals(board.getPlayerTurn(), "X");
        board.nextTurn();
        assertEquals(board.getPlayerTurn(), "O");
    }

    @Test
    public void testGameOverTokenOneWins() {
        board.setBoardSize(4);
        for (int i = 0; i < board.getBoardSize(); i++) {
            board.gameBoard[i][0] = new TokenOne();
        }
        assertTrue(board.gameOver());
        assertEquals("X is the Winner", board.getGameOverStatement());
    }

    @Test
    public void testGameOverTokenTwoWins() {
        Board board = new Board(4);
        for (int i = 0; i < board.getBoardSize(); i++) {
            board.gameBoard[i][0] = new TokenTwo();
        }
        assertTrue(board.gameOver());
        assertEquals("O is the Winner", board.getGameOverStatement());
    }

    @Test
    public void testGameOverBackWardDiagonalWin() {
        for (int i = 0; i < board.getBoardSize(); i++) {
            board.gameBoard[i][i] = new TokenOne();
        }
        assertTrue(board.gameOver());
//        assertTrue(board.checkWin(new TokenOne().getToken()));
        assertTrue(board.checkWin(new TokenOne()));
        assertEquals("X is the Winner", board.getGameOverStatement());
    }

    @Test
    public void testGameOverForwardDiagonalWin() {
        board.gameBoard[0][2] = new TokenOne();
        board.gameBoard[1][1] = new TokenOne();
        board.gameBoard[2][0] = new TokenOne();

        assertTrue(board.gameOver());
//        assertTrue(board.checkWin(new TokenOne().getToken()));
        assertTrue(board.checkWin(new TokenOne()));
        assertEquals("X is the Winner", board.getGameOverStatement());
    }

    @Test
    public void testGameOverDraw() {
        board.gameBoard[0][0] = new TokenTwo();
        board.gameBoard[0][1] = new TokenOne();
        board.gameBoard[0][2] = new TokenTwo();
        board.gameBoard[1][0] = new TokenOne();
        board.gameBoard[1][1] = new TokenOne();
        board.gameBoard[1][2] = new TokenTwo();
        board.gameBoard[2][0] = new TokenOne();
        board.gameBoard[2][1] = new TokenTwo();
        board.gameBoard[2][2] = new TokenOne();

        assertTrue(board.gameOver());
        assertEquals("The game was a draw", board.getGameOverStatement());
    }

    @Test
    public void testGameNotOver() {
        for (int i = 0; i < board.boardSize; i++) {
            for (int j = 0; j < board.boardSize; j++) {
                board.gameBoard[i][j] = new EmptyToken();
            }
        }
        assertTrue(!board.gameOver());
    }

    @Test
    public void testGameNotOverSomeTokens() {
        board.gameBoard[0][0] = new TokenOne();
        board.gameBoard[0][1] = new TokenTwo();
        board.gameBoard[0][2] = new TokenOne();
//        assertFalse(board.checkWin(new TokenOne().getToken()));
//        assertFalse(board.checkWin(new TokenTwo().getToken()));
        assertFalse(board.checkWin(new TokenOne()));
        assertFalse(board.checkWin(new TokenTwo()));
        assertFalse(board.gameOver());

        board.gameBoard[1][1] = new TokenOne();
        board.gameBoard[2][2] = new TokenTwo();
//        assertFalse(board.checkWin(new TokenOne().getToken()));
//        assertFalse(board.checkWin(new TokenTwo().getToken()));
        assertFalse(board.checkWin(new TokenOne()));
        assertFalse(board.checkWin(new TokenTwo()));
        assertFalse(board.gameOver());

        board.gameBoard[2][0] = new TokenTwo();
//        assertFalse(board.checkWin(new TokenOne().getToken()));
//        assertFalse(board.checkWin(new TokenTwo().getToken()));
        assertFalse(board.checkWin(new TokenOne()));
        assertFalse(board.checkWin(new TokenTwo()));
        assertFalse(board.gameOver());
    }

    @Test
    public void testCheckWinY() {
        for (int i = 0; i < board.boardSize; i++) {
            board.gameBoard[i][0] = new TokenOne();
        }
//        assertTrue(board.checkWin("X"));
        assertTrue(board.checkWin(new TokenOne()));
    }

    @Test
    public void testCheckWinX() {
        for (int i = 0; i < board.boardSize; i++) {
            board.gameBoard[0][i] = new TokenOne();
        }
//        assertTrue(board.checkWin("X"));
        assertTrue(board.checkWin(new TokenOne()));
    }

    @Test
    public void testCheckWinBackDiag() {
        board.gameBoard[0][0] = new TokenOne();
        board.gameBoard[1][1] = new TokenOne();
        board.gameBoard[2][2] = new TokenOne();
//        assertTrue(board.checkWin("X"));
        assertTrue(board.checkWin(new TokenOne()));
    }

    @Test
    public void testCheckWinForwardDiag() {
        board.gameBoard[0][2] = new TokenOne();
        board.gameBoard[1][1] = new TokenOne();
        board.gameBoard[2][0] = new TokenOne();
//        assertTrue(board.checkWin("X"));
        assertTrue(board.checkWin(new TokenOne()));
    }

}
