package ui.gui;

import javax.swing.*;
import java.awt.*;

// Frame for settings; lets user choose size of board
public class Settings {
    private final JFrame frame;
    private final Integer[] boardSizeOptionsLength;
    private JComboBox<Integer> comboBox;
    private JPanel settings;
    private JButton backBtn;
    private int sizeOfBoard;
    int upperBound;
    int lowerBound;

    // MODIFIES: this
    // EFFECTS: initializes frame, board size and interval, and settings layout and ui
    public Settings(int sizeOfBoard, JFrame frame) {
        this.frame = frame;
        this.sizeOfBoard = sizeOfBoard;

        upperBound = 10;
        lowerBound = 3;

        // --------------- ComboBox for settings UI ---------------
        boardSizeOptionsLength = new Integer[upperBound - lowerBound + 1];

        for (int i = 0; i <= upperBound - lowerBound; i++) {
            boardSizeOptionsLength[i] = (i + lowerBound);
        }
        // ----------------- ------------------------------

        settingsLayout();
        settingsUI();
    }

    // MODIFIES: this
    // EFFECTS: creates back button and comboBox action listeners
    public void settingsUI() {

        comboBox.addActionListener(e -> sizeOfBoard = comboBox.getSelectedItem() != null
                ? Integer.parseInt(comboBox.getSelectedItem().toString()) : 3);

        backBtn.addActionListener(e -> {
            backBtn.setVisible(false);
            comboBox.setVisible(false);
            settings.setVisible(false);
            new MainMenuFrame(frame, sizeOfBoard);
        });
    }

    // MODIFIES: this
    // EFFECTS: creates panel for text and organizes comoBox, back button and text in relation to each other
    public void settingsLayout() {
        settings = new JPanel();
        JLabel settingsTextP = new JLabel();
        settings.add(settingsTextP);
        settings.setBackground(new Color(100, 0, 100));
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        settingsTextP.setText("Choose the Board Size of your choice");
        settingsTextP.setFont(new Font("My Boli", Font.PLAIN, 20));
        settingsTextP.setForeground(new Color(100, 100, 200));
        frame.add(settings, c);

        c.gridy = 1;
        c.insets = new Insets(10, 0, 100, 0);
        comboBox = new JComboBox<>(boardSizeOptionsLength);
        frame.add(comboBox, c);
        comboBox.setSize(60, 60);
        comboBox.setPreferredSize(new Dimension(80, 40));
        comboBox.setFont(new Font("Serif", Font.PLAIN, 30));
        comboBox.setSelectedItem(sizeOfBoard);

        backBtn = new JButton("back");
        c.gridy = 2;
        c.insets = new Insets(150, 0, 0, 0);
        backBtn.setPreferredSize(new Dimension(100, 50));
        frame.add(backBtn, c);
    }
}
