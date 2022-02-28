package ui.gui;

import model.Board;
import model.EventLog;
import model.Event;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

// Main menu screen where the user starts
public class MainMenuFrame {
    private int sizeOfBoard;
    private Board board;

    private final JFrame frame;

    private JButton btn1;
    private JButton btn2;
    private JButton btn4;

    private JPanel menuPanel;



    // MODIFIES: this
    // EFFECTS: initializes frame, WindowLister, fields and layout
    public MainMenuFrame() {
        frame = new JFrame();
        WindowListener listener = new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {

                for (Event next : EventLog.getInstance()) {
                    System.out.println(next.toString() + "\n\n");
                }

            }
        };
        frame.addWindowListener(listener);
        initializeFields(3);
        initializeLayout();
    }

    // MODIFIES: this
    // EFFECTS: initializes frame, WindowLister, fields and layout
    public MainMenuFrame(JFrame frame, int sizeOfBoard) {
        this.frame = frame;
        WindowListener listener = new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {

                for (Event next : EventLog.getInstance()) {
                    System.out.println(next.toString() + "\n\n");
                }

            }
        };
        frame.addWindowListener(listener);
        initializeFields(sizeOfBoard);
        initializeLayout();
    }

    // REQUIRES: board size between 3 and 10 (inclusive)
    // MODIFIES: this
    // EFFECTS: initializes board and board size
    public void initializeFields(int sizeOfBoard) {
        this.sizeOfBoard = sizeOfBoard;

        board = new Board();
        board.setBoardSize(sizeOfBoard);


    }

    // MODIFIES: this
    // EFFECTS: initializes general layout of main menu frame
    public void initializeLayout() {
        try {
            final Image backgroundImage = javax.imageio.ImageIO.read(new File("./data/tictactoe.jpg"));
            frame.setContentPane(new JPanel(new BorderLayout()) {
                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            });
            frame.getContentPane().setPreferredSize(new Dimension(500, 500));
        } catch (IOException e) {
            String errorMessage = "Did not load background image";
            JOptionPane.showMessageDialog(frame, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
        }

        frame.setTitle("Tic-Tac-Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new GridBagLayout());

        // --------------- Panel for Menu UI ---------------
        menuLayout();
        // ----------------- ------------------------------
        menuUI(sizeOfBoard);
        frame.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes menu layout (i.e. background, size, buttons etc.)
    public void menuLayout() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(3, 1, 20, 35));
        menuPanel.setBackground(new Color(123, 50, 250));
        menuPanel.setPreferredSize(new Dimension(200, 370));
        frame.add(menuPanel);
        Border border = BorderFactory.createLineBorder(Color.blue, 3);
        menuPanel.setBorder(border);

        btn1 = new JButton("Play");
        btn2 = new JButton("Choose Board Size");
        btn4 = new JButton("Quit");

        btn1.setFocusable(false);
        btn2.setFocusable(false);
        btn4.setFocusable(false);

        menuPanel.add(btn1);
        menuPanel.add(btn2);
        menuPanel.add(btn4);
    }

    // MODIFIES: this, TicTacToeFrame
    // EFFECTS: sets up the main menu gui and calls the game frame when play button clicked
    public void menuUI(int sizeOfBoard) {

        btn1.addActionListener(e -> {
            menuPanel.setVisible(false);
            new TicTacToeFrame(sizeOfBoard, frame);
        });
        btn2.addActionListener(e -> {
            menuPanel.setVisible(false);
            new Settings(sizeOfBoard, frame);
        });
        btn4.addActionListener(e -> {
            frame.dispose();
            for (Event next : EventLog.getInstance()) {
                System.out.println(next.toString() + "\n\n");
            }
        });

    }

}
