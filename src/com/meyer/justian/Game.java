package com.meyer.justian;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.DefaultCaret;

public class Game extends JFrame
{

    private final Player[] players = { null, new RandomPlayer(), new RandomPlayer() };
    private Rack           rack;
    private ChipPanel[][]  chips;
    private JButton[]      buttons;
    private int            curPlayer;
    private int            turn;
    private JTextArea      log;

    public Game(String name)
    {
        super(name);
        setResizable(false);
        rack = new Rack();
        chips = new ChipPanel[rack.height()][rack.width()];
        buttons = new JButton[rack.width()];
        curPlayer = 1;
        turn = 0;
        log = new JTextArea(5, 20);
    }

    /*
     * Adds the main components to the main pane, including visualized rack, player buttons, and turn log.
     */
    public void addComponentsToPane(final Container pane)
    {
        JPanel rackVisual = new JPanel();
        JPanel controls = new JPanel();
        JPanel logPanel = new JPanel();
        rackVisual.setLayout(new GridLayout(rack.height(), rack.width()));
        controls.setLayout(new GridLayout(1, rack.width()));

        // Instantiate rack visual
        rackVisual.setPreferredSize(new Dimension(50 * rack.height(), 50 * rack.width()));
        for (int row = 0; row < rack.height(); row++) {
            for (int col = 0; col < rack.width(); col++) {
                chips[row][col] = new ChipPanel(rack.playerAt(row, col));
                rackVisual.add(chips[row][col]);
            }
        }

        // Instantiate controls
        for (int col = 0; col < rack.width(); col++) {
            JButton placeButton = new JButton(col + " ");
            controls.add(placeButton);
            placeButton.addActionListener(new ControlActionListener(col));
            buttons[col] = placeButton;
        }

        // Instantiate log
        JScrollPane scrollPane = new JScrollPane(log);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.black));
        log.setEditable(false);
        DefaultCaret caret = (DefaultCaret) log.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        logPanel.add(scrollPane);

        // Add panels
        pane.add(rackVisual, BorderLayout.NORTH);
        pane.add(controls, BorderLayout.CENTER);
        pane.add(scrollPane, BorderLayout.SOUTH);
    }

    /*
     * Set up and display the main GUI frame.
     */
    public static void createAndShowGUI(Game frame)
    {
        // Create and set up the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Set up the content pane
        frame.addComponentsToPane(frame.getContentPane());
        // Display window
        frame.pack();
        frame.setVisible(true);
    }

    /*
     * Enable/disable buttons based on whether it's a human player's turn.
     */
    public void setWhetherButtonsClickable()
    {
        for (JButton button : buttons) {
            button.setEnabled(players[curPlayer].isHuman());
        }
    }

    /*
     * Alternates between player's turns and applies appropriate updates.
     */
    public void incrementTurn()
    {
        turn++;
        curPlayer = (turn % 2 == 0) ? 1 : 2;

        if (players[curPlayer].isHuman()) {
            log.append("Player " + curPlayer + "'s turn.\n");
        }
        else {
            log.append("Player " + curPlayer + " is thinking...\n");
        }

        setWhetherButtonsClickable();
        updateRackPanel();
    }

    /*
     * Creates a GUI for the game and begins game logic.
     */
    public static void main(String[] args)
    {
        final Game game = new Game("Connect Four!");

        // Set look and feel to default system settings
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
        catch (InstantiationException ex) {
            ex.printStackTrace();
        }
        catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        // Schedule a job for the event dispatch thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run()
            {
                createAndShowGUI(game);
            }
        });

        game.runGame();
    }

    /*
     * Main game logic. Non-threaded.
     */
    public void runGame()
    {
        log.append("Player " + curPlayer + "'s turn!\n");

        while (!rack.isGameOver()) {
            if (!players[curPlayer].isHuman()) {
                rack.place(curPlayer, players[curPlayer].getMove(rack));
                System.out.println(rack.toString(false));
                incrementTurn();
            }
        }

        log.append("Turn: " + turn + ", Winner: " + rack.winner() + "\n");
    }

    /*
     * Updates the visual game rack component.
     */
    public void updateRackPanel()
    {
        for (int row = 0; row < rack.height(); row++) {
            for (int col = 0; col < rack.width(); col++) {
                chips[row][col].setPlayer(rack.playerAt(row, col));
                chips[row][col].repaint();
            }
        }
    }

    /*
     * Performs a player move on action.
     */
    private class ControlActionListener implements ActionListener
    {
        private int col;

        public ControlActionListener(int col)
        {
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (players[curPlayer].isHuman() && rack.movePossible(col)) {
                rack.place(curPlayer, col);
                incrementTurn();
            }
        }
    }

}
