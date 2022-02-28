package ui;

//import main.model.*;

import exceptions.BoardPositionOccupiedException;
import exceptions.WrongInputAtPlayMenuException;
import model.Board;
import model.BoardRoom;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// Game mechanics such as getting user input and orchestrating the other classes together
public class TicTacToe {

    // Initialize variables
    private Scanner input;
    private Board board;
    private String[][] playLog;
    private int gameBoardSize;
    private boolean loadedGame;
    private boolean keepGoing;

    private static final String JSON_STORE = "./data/boardroom.json";
    private BoardRoom boardRoom;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;


    // Initialize values
    public TicTacToe() {
        boardRoom = new BoardRoom("Joel's boardroom");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        board = new Board();
        loadedGame = false;

        gameBoardSize = board.getBoardSize();
        this.playLog = new String[gameBoardSize][gameBoardSize];
        for (int i = 0; i < gameBoardSize; i++) {
            for (int j = 0; j < gameBoardSize; j++) {
                playLog[i][j] = " " + j + "," + i + " ";
            }
        }

        // Run Board Room
        runBoardRoom();

    }

    // MODIFIES: this, board
    // EFFECTS: gets user inputs and put tokens on corresponding square
    public void playGame() { //} throws WrongInputAtPlayMenuException {
        input = new Scanner(System.in);

        if (!loadedGame) {
            board = new Board();
            playLogReset();
            chooseStartingToken();
        } else {
            gameLoadedDisplayPlayMenu();
        }
        showGameBoard();
        addTokenToGamePos();
        while (!board.gameOver() && keepGoing) {
            showGameBoard();

            System.out.println("Select from: \n " + "\tb to go back to the display menu");
            System.out.println("\tc to continue");
            String choose = input.nextLine();

            playGameExceptionHandling(choose);

            if (choose.equals("b")) {
                //runBoardRoom();
                return;
            } else if (choose.equals("c")) {
                addTokenToGamePos();
            }
        }

        System.out.println("Game Over: " + board.getGameOverStatement());
    }

    // EFFECTS: throws exception if wrong input is received from user on play game menu
    public void playGameExceptionHandling(String choose) {
        try {
            if (!choose.equals("b") && !choose.equals("c")) {
                throw new WrongInputAtPlayMenuException();
            }
        } catch (WrongInputAtPlayMenuException w) {
            System.out.println("Invalid input. Please select either b or c");
        }
    }

    // MODIFIES: this
    // EFFECTS: displays tic-tac-toe board
    public void showGameBoard() {
        System.out.println(playLog[0][0] + "|" + playLog[0][1] + "|" + playLog[0][2] + " ");
        System.out.println("_" + " " + "_" + " " + "_" + " " + "_" + "_" + " " + "_" + " " + "_" + " " + "_" + "_"
                + " " + "_");
        System.out.println(playLog[1][0] + "|" + playLog[1][1] + "|" + playLog[1][2] + " ");
        System.out.println("_" + " " + "_" + " " + "_" + " " + "_" + "_" + " " + "_" + " " + "_" + " " + "_" + "_"
                + " " + "_");
        System.out.println(playLog[2][0] + "|" + playLog[2][1] + "|" + playLog[2][2]);
    }


    // MODIFIES: board
    // EFFECTS: sets initial starting token to what the user chooses
    public void chooseStartingToken() {
        String startTok = "";
        System.out.println("Please select either X or O to continue");
        System.out.println("Please select b to to back to the display menu");

        startTok = input.nextLine();

        while (!startTok.equals("X") && !startTok.equals("O") && !startTok.equals("b")) {
            try {
                throw new WrongInputAtPlayMenuException();
            } catch (WrongInputAtPlayMenuException e) {
                System.out.println("Invalid input");
                System.out.println("Please select either X or O to continue");
                System.out.println("Please select b to to back to the display menu");
                startTok = input.nextLine();
            }
        }


        if (startTok.equals("b")) {
            runBoardRoom();
        } else {
            if (!startTok.equals(board.getPlayerTurn())) {
                board.nextTurn();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runBoardRoom() {
        //boolean keepGoing = true;
        keepGoing = true;
        String command = null;
        input = new Scanner(System.in);

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> add board");
        //System.out.println("\tp -> print boards");
        System.out.println("\tp -> play board game");
        // System.out.println("\ts -> save board room to file");
        System.out.println("\ts -> save board game to file");
        //System.out.println("\tl -> load board room from file");
        System.out.println("\tl -> load board room from file");
        System.out.println("\tq -> quit");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("a")) {
            addBoard();
        } else if (command.equals("p")) {
            //printBoards();
            // try {
            playGame();
            //} catch (WrongInputAtPlayMenuException w) {
            //  System.out.println("Wrong input received");
            //}
        } else if (command.equals("s")) {
            saveBoardRoom();
        } else if (command.equals("l")) {
            loadBoardRoom();
            loadedGame = true;
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: prompt user for name of board and adds to boardroom
    private void addBoard() {
        input = new Scanner(System.in);
        System.out.println("Please name your board: ");
        String name = input.nextLine();
        board.setBoardName(name);

        boardRoom.addBoard(board);
    }

    // EFFECTS: prints all the thingies in boardroom to the console
    private void printPlayGameMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\n\t0 -> Play new game");
        System.out.println("\n\tTo play old game, please choose the board with which to play:");
        for (int i = 0; i < boardRoom.numBoards(); i++) {
            System.out.println("\t " + (i + 1) + " -> to choose " + boardRoom.getBoards().get(i).getBoardName());
        }
    }

    // EFFECTS: saves the boardroom to file
    private void saveBoardRoom() {
        try {
            jsonWriter.open();
            jsonWriter.write(boardRoom);
            jsonWriter.close();
            System.out.println("Saved " + boardRoom.getName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads boardroom from file
    private void loadBoardRoom() {
        try {
            boardRoom = jsonReader.read();
            chooseBoard(0);
            System.out.println("Loaded " + boardRoom.getName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: Choose board among saved to continue game on
    public void chooseBoard(int n) {
        board = boardRoom.getBoards().get(n);
        for (int i = 0; i < gameBoardSize; i++) {
            for (int j = 0; j < gameBoardSize; j++) {

                if (board.getTokenFromArr(j, i).equals("∅")) {
                    playLog[i][j] = " " + j + "," + i + " ";
                } else {
                    playLog[i][j] = "  " + board.getTokenFromArr(j, i) + "  ";
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Resets play log for new game
    public void playLogReset() {
        for (int i = 0; i < gameBoardSize; i++) {
            for (int j = 0; j < gameBoardSize; j++) {
                playLog[i][j] = " " + j + "," + i + " ";
            }
        }
    }

    // EFFECTS: Displays play menu when game boards have been loaded
    public void gameLoadedDisplayPlayMenu() {
        printPlayGameMenu();
        input = new Scanner(System.in);

        boolean error = true;

        while (error) {
            try {
                int optionChosen = input.nextInt();
                boolean intInRange = optionChosen > 0 && optionChosen <= boardRoom.numBoards();
                if (!intInRange && optionChosen != 0) {
                    throw new WrongInputAtPlayMenuException();
                }
                input.nextLine();
                playGameValidChoices(intInRange, optionChosen);
                error = false;
            } catch (Exception e) {
                System.out.println("Invalid input");
                System.out.println("Please select either 0 or one of the boards to continue");
                input.nextLine();
            }

        }
    }


    // REQUIRES: intInRange is of type boolean and 0 <= optionChosen <= boardRoom.numBoards()
    // MODIFIES: this
    // EFFECTS: chooses a new board if 0 is picked or one of the loaded boards otherwise
    public void playGameValidChoices(boolean intInRange, int optionChosen) {
        if (intInRange) {
            chooseBoard(optionChosen - 1);
        } else if (optionChosen == 0) {
            board = new Board();
            playLogReset();
            chooseStartingToken();
        }
    }

    // EFFECTS: returns array of x and y position chosen by user
    public String[] positionChoose() {
        System.out.println("Please select the x position of your chosen square: ");
        String posX = input.nextLine();
        String positionX = posExceptions(posX);
        System.out.println(positionX);

        System.out.println("Please select the y position of your chosen square: ");
        String posY = input.nextLine();
        String positionY = posExceptions(posY);
        System.out.println(positionY);
        String[] returnArr = {positionX, positionY};
        return returnArr;
    }

    // MODIFIES: this
    // EFFECTS: tokens are placed on board grid based on position chosen by player
    public void addTokenToGamePos() {

        boolean error = true;
        while (error) {
            try {
                String[] posChoose = positionChoose();

                int posXInt = Integer.parseInt(posChoose[0]);
                int posYInt = Integer.parseInt(posChoose[1]);
                if (board.getPlayerTurn().equals("X")) {
                    board.addXs(posXInt, posYInt);
                } else if (board.getPlayerTurn().equals("O")) {
                    board.addOs(posXInt, posYInt);
                }
                playLog[posYInt][posXInt] = "  " + board.getPlayerTurn() + "  ";
                board.nextTurn();
                error = false;
            } catch (BoardPositionOccupiedException e) {
                System.out.println("Position occupied. Please select another.");
            }
        }
    }

    // EFFECTS: takes in input on x or y position picked by user and returns if valid
    public String posExceptions(String pos) {
        String position = pos;
        while (!position.equals("0") && !position.equals("1")) {
            try {
                throw new WrongInputAtPlayMenuException();
            } catch (WrongInputAtPlayMenuException w) {
                System.out.println("Invalid input. Please input your position on the square");
                position = input.nextLine();
            }
        }
        return position;
    }

}
