package model;

import exceptions.BoardPositionOccupiedException;
import org.json.JSONObject;
import persistence.Writable;

// This is where the game functions of a tic-tac-toe board are put together
public class Board implements Writable {


    // Default size
    int boardSize;

    protected GameToken[][] gameBoard;
    private boolean gameOver;
    private EmptyToken emptyToke;
    private TokenOne tokenOne;
    private TokenTwo tokenTwo;
    protected String playerTurn;
    private String gameOverStatement;

    private String name;

    // REQUIRES:
    // EFFECTS: this
    // MODIFIES: initializes values
    public Board() {
        boardSize = 3;
        this.gameBoard = new GameToken[boardSize][boardSize];
        this.gameOver = false;
        this.emptyToke = new EmptyToken();
        this.tokenTwo = new TokenTwo();
        this.tokenOne = new TokenOne();
        playerTurn = tokenOne.getToken();

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                gameBoard[i][j] = emptyToke;
            }
        }
    }

    // REQUIRES:
    // EFFECTS: this
    // MODIFIES: initializes values
    public Board(int boardSize) {
        this.boardSize = boardSize;
        this.gameBoard = new GameToken[boardSize][boardSize];
        this.gameOver = false;
        this.emptyToke = new EmptyToken();
        this.tokenTwo = new TokenTwo();
        this.tokenOne = new TokenOne();
        playerTurn = tokenOne.getToken();

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                gameBoard[i][j] = new EmptyToken();
            }
        }
    }



    // EFFECTS: returns board size
    public int getBoardSize() {
        return this.boardSize;
    }

    // REQUIRES: boardsize positive integer greater than 2
    // EFFECTS: sets board size
    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    // EFFECTS: returns game board
    public GameToken[][] getGameBoard() {
        return this.gameBoard;
    }

    // EFFECTS: sets board tile equal to some token
    public void setGameBoard(int posX, int posY, GameToken tok) {
        this.gameBoard[posY][posX] = tok;
    }

    // EFFECTS: returns player turn
    public String getPlayerTurn() {
        return this.playerTurn;
    }

    // MODIFIES: this
    // EFFECTS: sets the playerTurn
    public void setPlayerTurn(String playerTurn) {
        this.playerTurn = playerTurn;
    }


    // MODIFIES: this
    // EFFECTS: sets the board name
    public void setBoardName(String name) {
        this.name = name;
    }

    // EFFECTS: returns board name
    public String getBoardName() {
        return this.name;
    }


    // REQUIRES: posX and posY between 0 and 2
    // MODIFIES: this
    // EFFECTS: inserts instance of Xs object on gameBoard
    //            P.S: posY is row, posX is column
    public void addXs(int posX, int posY) throws BoardPositionOccupiedException {
        if (!this.gameBoard[posY][posX].getToken().equals("∅")) {
            throw new BoardPositionOccupiedException();
        }
        this.gameBoard[posY][posX] = new TokenOne();
    }

    // REQUIRES: posX and posY between 0 and 2
    // MODIFIES: this
    // EFFECTS: inserts instance of Os object on gameBoard
    public void addOs(int posX, int posY) throws BoardPositionOccupiedException {
        if (!this.gameBoard[posY][posX].getToken().equals("∅")) {
            throw new BoardPositionOccupiedException();
        }
        this.gameBoard[posY][posX] = new TokenTwo();
    }


    // REQUIRES: posX and posY between 0 and 2
    // MODIFIES: emptyToke, TokenOne, TokenTwo
    // EFFECTS: returns the string token at a certain position
    public String getTokenFromArr(int posX, int posY) {
        String getToke;
//        if (gameBoard[posY][posX] == null) {
//            getToke = emptyToke.getToken();
//        } else {
        getToke = gameBoard[posY][posX].getToken();
//        }
        return getToke;
    }

    // REQUIRES: posX and posY between 0 and 2
    // MODIFIES: emptyToke, TokenOne, TokenTwo
    // EFFECTS: returns the token's value at a certain position
    public int getValueFromArr(int posX, int posY) {
        int getVal;

        getVal = gameBoard[posY][posX].getVal();
//        }
        return getVal;
    }

    // REQUIRES:
    // MODIFIES: this, TokenOne, TokenTwo
    // EFFECTS: changes playerTurn from "X" to "O" or "O" to "X"
    public void nextTurn() {
        if (playerTurn.equals(tokenOne.getToken())) {
            playerTurn = tokenTwo.getToken();
        } else {
            playerTurn = tokenOne.getToken();
        }
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: returns true if game is over, false otherwise
    public boolean gameOver() {
        boolean winOne = checkWin(tokenOne);
        boolean winTwo = checkWin(tokenTwo);

        boolean win = winOne || winTwo;
        this.gameOver = true;
        if (win) {
            this.gameOverStatement = (winOne) ? tokenOne.getToken() : tokenTwo.getToken();
            this.gameOverStatement += " is the Winner";
        } else {
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    if (getTokenFromArr(j, i).equals(new EmptyToken().getToken())) {
                        this.gameOver = false;
                        return false;
                    }
                }
            }
            this.gameOverStatement = "The game was a draw";
        }
        return true;
    }

    // EFFECTS: returns game over statement
    public String getGameOverStatement() {
        return this.gameOverStatement;
    }


    // REQUIRES: A token string which is either "X" or "O"
    // MODIFIES: this
    // EFFECTS: True if player with certain token wins, false otherwise
    public boolean checkWin(GameToken gameToken) {
        for (int i = 0; i < this.boardSize; i++) {
            int totalRow = 0;
            int totalCol = 0;
            int totDiagFor = 0;
            int totDiagBack = 0;
            for (int j = 0; j < this.boardSize; j++) {
                totalRow += getValueFromArr(j, i);
                totalCol += getValueFromArr(i, j);
                totDiagFor += getValueFromArr(j, (this.boardSize - 1) - j);
                totDiagBack += getValueFromArr(j, j);
            }
            int winVal = gameToken.getVal() * this.boardSize;
            if (totalRow == winVal || totalCol == winVal || totDiagFor == winVal || totDiagBack == winVal) {
                gameOver = true;
                return true;
            }
        }
        return false;
    }

    // MODIFIES: JSONOBJECT
    // EFFECTS: saves key data to json such as playerTurn, gameBoard, and name of board
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("gameBoard", gameBoard);
        json.put("playerTurn", playerTurn);
        json.put("name", name);
        //json.put("EntireBoard", this);
        return json;
    }

}
