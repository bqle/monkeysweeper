package org.cis120.minesweeper;

/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.LinkedList;

/**
 * This class instantiates a TicTacToe object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Minesweeper minesweeper; // model for the game
    private JLabel status; // current status text
    private BufferedImage flagImg;
    private BufferedImage hiddenImg;
    private BufferedImage bombImg;
    private JButton[] buttons;
    private boolean solvingMode;

    // Game constants
    public static final int BOARD_WIDTH = 400;
    public static final int BOARD_HEIGHT = 400;


    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit, JButton[] buttonsInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        status = statusInit; // initializes the status JLabel
        buttons = buttonsInit;
        solvingMode = false;
        minesweeper = readFromFile(); // initializes model for the game

        updateStatus();

        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (!solvingMode) {
                    Point p = e.getPoint();
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        minesweeper.show(p.y / 20, p.x / 20);
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        minesweeper.flipFlag(p.y / 20, p.x / 20);
                    }

                    updateStatus(); // updates the status JLabel
                    repaint(); // repaints the game board
                }
            }
        });
        try {
            flagImg = ImageIO.read(new File("files/flag.png"));
            hiddenImg = ImageIO.read(new File("files/hidden.png"));
            bombImg = ImageIO.read(new File("files/bomb.jpeg"));
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    public void saveToFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("files/save.txt"));
            bw.write(minesweeper.toString());
            bw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Minesweeper readFromFile() {
        Minesweeper m = new Minesweeper();
        Square[][] board = m.getBoard();

        File f = new File("files/save.txt");
        try{
            if (f.exists()) {
                System.out.println("File exists");
                BufferedReader br = new BufferedReader(new FileReader("files/save.txt"));
                m.setFlagCount(Integer.valueOf(br.readLine()));
                m.setUncoveredCnt(Integer.valueOf(br.readLine()));
                m.setGameOver(Boolean.valueOf(br.readLine()));
                m.setBombCount(Integer.valueOf(br.readLine()));
                for (int i = 0 ; i < 20 ;i++) {
                    String row = br.readLine();
                    if (row == null || row.isEmpty()) {
                        break;}
                    String[] squareStrings = row.split(";");
                    int min = Math.min(20, squareStrings.length);
                    for (int j = 0; j < min; j++) {
                        String[] squareStr = squareStrings[j].split(",");
                        board[i][j].setValue(Integer.valueOf(squareStr[2]));
                        board[i][j].setVisibility(Integer.valueOf(squareStr[3]));
                    }
                }
                br.close();
            }
        }catch (Exception e) {
            System.out.println("save file not found, or file was in incorrect format");
            System.out.println(e);
        } finally {
            return m;
        }
    }
    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        minesweeper.reset();
        status.setText("Flag: " + minesweeper.getFlagCount());
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    public void switchSolvingMode() {
        if (solvingMode) {
            for (JButton b : buttons) {
                b.setEnabled(true);
            }
        } else {
            for (JButton b : buttons) {
                b.setEnabled(false);
            }
        }
        solvingMode = !solvingMode;
    }

    /**
     * performs all clicks
     */
    public void performAllClicks(LinkedList<Square> clicks) {
        System.out.println("perform all clicks: " + clicks.size());
        for (Square s: clicks) {
            System.out.println(s.getI() + " " + s.getJ());
            int temp_i = s.getI();
            int temp_j = s.getJ();
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    minesweeper.show(temp_i, temp_j);
                    status.setText("Just played row: " + temp_i + " col:" + temp_j);
                    repaint();
                }
            });
            try {
                Thread.sleep(200);
            } catch(InterruptedException e) {
                System.out.println("Interrupted exception");
            }
        }
        updateStatus();
    }

    /**
     * Solve the game using standard human techniques
     * except the ability to solving non-unique situations
     * accounting for flag count
     */
    public void solveLikePro() {
        solve(minesweeper.solveLikePro());
    }


    /**
     * Solve the game without acknowledging guaranteeed squares
     */
    public void solveLikeAmateur() {
        solve(minesweeper.solveLikeAmateur());
    }

    /**
     * Solve the game like a monkey
     */
    public void solveLikeMonkey() {
        solve(minesweeper.solveLikeMonkey());
    }

    /**
     * general helper to activate all the clicks one-by-one
     * @param clicks
     */
    public void solve(LinkedList<Square> clicks) {
        try {
            new Thread(new Runnable() {
                public void run() {
                    switchSolvingMode();
                    performAllClicks(clicks);
                    switchSolvingMode();
                }
            }).start();

        }catch (Exception e) {
            System.out.println("Interrupted exception");
        }
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (minesweeper.checkWin()) {
            minesweeper.revealAllSquares();
            status.setText("Congratulations! You won!");
        } else if (minesweeper.isGameOver()) {
            status.setText("Game over! You hit a bomb.");
        } else {
            status.setText("Flags: " + minesweeper.getFlagCount());
        }
    }

    /**
     * Draws the game board.
     * 
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw vertical lines
        for (int i = 1; i < 21; i++) {
            g.drawLine(20 * i, 0, 20 * i, BOARD_HEIGHT);
        }
        // Draw horizontal lines
        for (int i = 1; i < 21; i++) {
            g.drawLine(0, 20*i, BOARD_WIDTH, 20*i);
        }

        // Draws flags and numbers
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                Square square = minesweeper.getSquare(j, i);
                int value = square.getValue();
                int visibility = square.getVisibility();

                if (visibility == 0) {
                    g.drawImage(hiddenImg, 20*i, 20*j, 20, 20, null);
                } else if (visibility == 1) {
                    if (value == -1) {
                        g.drawImage(bombImg, 20*i+1, 20*j+1, 19, 19, null);
                    } else if (value > 0){
                        g.drawString(Integer.toString(value), 20 * i+7, 20 * j + 15);
                    }
                } else {
                    g.drawImage(flagImg, 20 * i, 20 * j, 20, 20, null);
                }

            }
        }


    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
