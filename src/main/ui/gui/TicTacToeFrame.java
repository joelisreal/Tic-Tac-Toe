package ui.gui;

import exceptions.BoardPositionOccupiedException;
import model.Board;
import model.BoardRoom;
import model.Event;
import model.EventLog;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// Contains game gui
public class TicTacToeFrame implements ActionListener {
    private int sizeOfBoard;
    private Board board;
    private JPanel gameMenuBarPanel;
    private JPanel gamePanel;
    private JLabel gameOverLabel;
    private JPanel gameOverPanel;
    private final JFrame frame;
    private String input;
    private JComboBox<String> comboBox;
    private int boardChosen;
    private String boardChosenStr;
    private String[] boardNames;


    private static final String JSON_STORE = "./data/boardroom.json";
    private BoardRoom boardRoom;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JTextField field;

    JButton backButton;
    JButton loadButton;
    JButton saveButton;
    ArrayList<ArrayList<JButton>> btn;
    GridBagConstraints constraints;
    boolean loadButtonClicked;

    // MODIFIES: this
    // EFFECTS: initializes frame, board size, board object and calls buttons() and ticTacToePanel()
    public TicTacToeFrame(int sizeOfBoard, JFrame frame) {
        initializeFields();

        frame.setContentPane(new JPanel(new BorderLayout()) {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                revalidate();
                repaint();
            }
        });


        this.frame = frame;
        this.sizeOfBoard = sizeOfBoard;
        btn = new ArrayList<>();

        // populate arraylist with values
        for (int i = 0; i < sizeOfBoard; i++) {
            btn.add(new ArrayList<>());
            for (int j = 0; j < sizeOfBoard; j++) {
                btn.get(i).add(new JButton());
            }
        }
        board = new Board(sizeOfBoard);

        initializeLayout();
    }

    // MODIFIES: this
    // EFFECTS: initializes new boardroom, json file writer and reader, and initial state of load button
    public void initializeFields() {
        boardRoom = new BoardRoom("Joel's boardroom");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        loadButtonClicked = false;
    }

    // MODIFIES: this
    // EFFECTS: creates general layout of game board frame
    public void initializeLayout() {
        frame.setSize(100 + 100 * sizeOfBoard, 100 + 100 * sizeOfBoard);
        frame.getContentPane().setBackground(new Color(123, 50, 250));
        frame.setLayout(new GridBagLayout());
        buttons();
        gamePanelSetup();
        ticTacToePanel();
        frame.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: sets up the game panel and customizes with background color and generates overall layout
    public void gamePanelSetup() {
        gamePanel = new JPanel();

        gamePanel.setLayout(new GridLayout(sizeOfBoard, sizeOfBoard));

        gamePanel.setBackground(new Color(50, 147, 250));
        gamePanel.setPreferredSize(new Dimension(70 * sizeOfBoard, 70 * sizeOfBoard));

        constraints = new GridBagConstraints();

        constraints.gridy = 1;
        frame.add(gamePanel, constraints);


        gameOverPanel = new JPanel();

        gameOverLabel = new JLabel();
        gameOverLabel.setText("Game Over: Not yet");

        Border border = BorderFactory.createLineBorder(Color.green, 3);
        gameOverLabel.setBorder(border);


        gameOverPanel.add(gameOverLabel);
        constraints.gridy = 2;
        constraints.insets = new Insets(10, 0, 0, 0);
        frame.add(gameOverPanel, constraints);
    }

    // MODIFIES: this
    // EFFECTS: board gui and game logic are generated here; implemented via actionListener
    public void ticTacToePanel() {
        JButton b;
        for (int i = 0; i < sizeOfBoard; i++) { //ArrayList<JButton> bt : btn) {
            for (int j = 0; j < sizeOfBoard; j++) { //for (JButton b : bt) {
                b = btn.get(i).get(j);
                gamePanel.add(b);
                b.setFocusable(false);

                b.addActionListener(this);
            }
        }
    }

    // REQUIRES: button on board to be clicked
    // MODIFIES: this, Board
    // EFFECTS: assigns values to board gui and board object; checks for when gameOver
    @Override
    public void actionPerformed(ActionEvent e) {
        for (int posYInt = 0; posYInt < sizeOfBoard; posYInt++) {
            for (int posXInt = 0; posXInt < sizeOfBoard; posXInt++) {
                if (e.getSource() == btn.get(posYInt).get(posXInt)) {
                    if (!board.gameOver()) {
                        JButton b = btn.get(posYInt).get(posXInt);
                        addTokenToBoardObject(posXInt, posYInt, b);
                    }
                    if (board.gameOver()) {
                        gameOverLabel.setText("Game Over: " + board.getGameOverStatement());
                    }
                }
            }
        }
    }

    // REQUIRES: X and Y position of token
    // MODIFIES: this, Board
    // EFFECTS: adds tokens to board object; checks that position not occupied
    public void addTokenToBoardObject(int posXInt, int posYInt, JButton b) {
        try {
            if (board.getPlayerTurn().equals("X")) {
                board.addXs(posXInt, posYInt);
                EventLog.getInstance().logEvent(new Event("Added an X at row " + posYInt + " and column "
                        + posXInt));
                b.setText(board.getPlayerTurn());
                board.nextTurn();
            } else if (board.getPlayerTurn().equals("O")) {
                board.addOs(posXInt, posYInt);
                EventLog.getInstance().logEvent(new Event("Added an O at row " + posYInt + " and column "
                        + posXInt));
                b.setText(board.getPlayerTurn());
                board.nextTurn();
            }
        } catch (BoardPositionOccupiedException ex) {
            System.out.println("Position occupied. Please select another.");
            String errorMessage = "Position occupied. Please select another.";
            JOptionPane.showMessageDialog(frame, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: calls back, save and load buttons; adds menuBarPanel to frame
    public void buttons() {
        gameMenuBarPanel = new JPanel();
        backButtonUI();
        loadButtonUI();
        saveButtonUI();
        frame.add(gameMenuBarPanel);
    }

    // MODIFIES: this, mainMenuFrame
    // EFFECTS: creates new mainMenuFrame when clicked
    public void backButtonUI() {
        backButton = new JButton("back");
        constraints = new GridBagConstraints();
        gameMenuBarPanel.add(backButton);
        backButton.addActionListener(e -> {
            new MainMenuFrame(frame, sizeOfBoard);
            EventLog.getInstance().logEvent(new Event("Went back to main menu thus clearing board"));
        });

    }

    // MODIFIES: this
    // EFFECTS: loads board from file when clicked
    public void loadButtonUI() {
        loadButton = new JButton("load");
        gameMenuBarPanel.add(loadButton);
        loadButton.addActionListener(e -> {
            if (!loadButtonClicked) {
                loadBoardRoom();
            }
            loadButtonClicked = true;

        });
    }

    // MODIFIES: this
    // EFFECTS: save board to file when clicked
    public void saveButtonUI() {
        saveButton = new JButton("save");
        constraints = new GridBagConstraints();
        gameMenuBarPanel.add(saveButton);
        saveButton.addActionListener(e -> {
            field = new JTextField(5);
            board.setBoardName(field.getText());
            saveBoardRoom();
        });
    }

    // MODIFIES: this, board
    // EFFECTS: loads boardroom from file
    private void loadBoardRoom() {
        try {
            boardRoom = jsonReader.read();
            boardChosen = 0;
            boardChosenStr = "";
            boardNames = new String[boardRoom.numBoards()];
            for (int i = 0; i < boardRoom.getBoards().size(); i++) {
                boardNames[i] = boardRoom.getBoards().get(i).getBoardName();
            }

            dropDownMenuLoadBoardRoom();
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    public void dropDownSetUp() {
        comboBox = new JComboBox<>(boardNames);
        gameMenuBarPanel.add(comboBox);
        comboBox.setSize(60, 60);
        comboBox.setPreferredSize(new Dimension(80, 40));
        comboBox.setFont(new Font("Serif", Font.PLAIN, 30));
        comboBox.setSelectedItem(boardChosenStr);
        comboBox.setEditable(true);
    }

    // MODIFIES: this, comboBox
    // EFFECTS: creates drop down menu when load bar is clicked, after boardroom has been loaded
    public void dropDownMenuLoadBoardRoom() {
        dropDownSetUp();

        comboBox.addActionListener(e -> {
            if (comboBox.getSelectedItem() == null) {
                String message = "Selected Item is null";
                JOptionPane.showMessageDialog(null, message);
            } else {
                boardChosenStr = (comboBox.getSelectedItem().toString());
            }
            for (int i = 0; i < boardRoom.getBoards().size(); i++) {
                if (boardRoom.getBoards().get(i).getBoardName().equals(boardChosenStr)) {
                    boardChosen = i;
                    chooseBoard(boardChosen);
                    String message = "Loaded " + boardRoom.getName() + " from " + JSON_STORE;
                    JOptionPane.showMessageDialog(null, message);
                    gameMenuBarPanel.remove(comboBox);
                    loadButtonClicked = false;
                    break;
                }
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: resets game board after saved game has been loaded
    public void loadBoardReset() {
        gamePanel.removeAll();
        gamePanel.revalidate();
        gamePanel.repaint();

        btn = new ArrayList<>();

        for (int i = 0; i < sizeOfBoard; i++) {
            btn.add(new ArrayList<>());
            for (int j = 0; j < sizeOfBoard; j++) {
                btn.get(i).add(new JButton());
            }
        }
        for (ArrayList<JButton> bt : btn) {
            for (JButton b : bt) {
                gamePanel.add(b);
                b.setFocusable(false);

                b.addActionListener(this);
            }
        }
    }

    // MODIFIES: this, board
    // EFFECTS: Choose board among saved to continue game on
    public void chooseBoard(int n) {
        board = boardRoom.getBoards().get(n);
        this.sizeOfBoard = board.getBoardSize();
        loadBoardReset();
        gamePanel.setLayout(new GridLayout(sizeOfBoard, sizeOfBoard));
        gamePanel.setPreferredSize(new Dimension(70 * sizeOfBoard, 70 * sizeOfBoard));
        for (int i = 0; i < sizeOfBoard; i++) {
            for (int j = 0; j < sizeOfBoard; j++) {

                if (board.getTokenFromArr(j, i).equals("∅")) {
                    btn.get(i).get(j).setText("");
                } else {
                    btn.get(i).get(j).setText(board.getTokenFromArr(j, i));
                    System.out.println(board.getTokenFromArr(j, i));
                }
            }
        }
        frame.setSize(100 + 100 * sizeOfBoard, 100 + 100 * sizeOfBoard);
    }

    // MODIFIES: this, board
    // EFFECTS: saves the boardroom to file
    private void saveBoardRoom() {
        try {
            addBoard();
            jsonWriter.open();
            jsonWriter.write(boardRoom);
            jsonWriter.close();
            System.out.println("Saved " + boardRoom.getName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this, board
    // EFFECTS: prompt user for name of board and adds to boardroom
    private void addBoard() {
        input = JOptionPane.showInputDialog("Please name your board:");
        board.setBoardName(input);

        boardRoom.addBoard(board);
    }

}