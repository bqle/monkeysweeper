package org.cis120.minesweeper;

/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, Game initializes the view,
 * implements a bit of controller functionality through the reset
 * button, and then instantiates a GameBoard. The GameBoard will
 * handle the rest of the game's view and controller functionality, and
 * it will instantiate a TicTacToe object to serve as the game's model.
 */
public class RunMinesweeper implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("TicTacToe");
        frame.setLocation(400, 400);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Menu
        final JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        frame.add(menu, BorderLayout.WEST);

        // Control panel
        final JPanel control_panel = new JPanel();
        control_panel.setLayout(new BoxLayout(control_panel, BoxLayout.Y_AXIS));
        control_panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 145, 0));

        final JPanel help_panel = new JPanel();
        help_panel.setLayout(new BoxLayout(help_panel, BoxLayout.Y_AXIS));

        menu.add(control_panel);
        menu.add(help_panel);

        final JButton reset = new JButton("Reset");
        final JButton solveLikePro = new JButton("<html>Solve<br>like<br>pro</html>");
        final JButton solveLikeAmateur = new JButton("<html>Solve<br>like<br>amateur</html>");
        final JButton solveLikeMonkey = new JButton("<html>Solve<br>like<br>monkey</html>");
        final JButton save = new JButton("<html>Save</html>");
        final JButton help = new JButton("<html>Help</html>");

        JButton[] buttons = new JButton[]{reset, solveLikePro, solveLikeAmateur, solveLikeMonkey, save, help};

        // Game board
        final GameBoard board = new GameBoard(status, buttons);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.reset();
            }
        });
        control_panel.add(reset);

        // SolveLikePro button
        solveLikePro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.solveLikePro();
            }
        });
        control_panel.add(solveLikePro);

        // SolveLikeAmateur button
        solveLikeAmateur.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.solveLikeAmateur();
            }
        });
        control_panel.add(solveLikeAmateur);

        // SolveLikeMonkey button
        solveLikeMonkey.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.solveLikeMonkey();
            }
        });
        control_panel.add(solveLikeMonkey);

        // Save button
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.saveToFile();
            }
        });
        help_panel.add(save);

        // Help button
        help.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame,
                        "<html><center>Welcome to <i>Minesweeper V2</i></center><br>" +
                                "Left click to reveal a square<br>" +
                                "Right click to place or remove a flag<br>" +
                                "The side options come with abilities<br>" +
                                "of increasing dexterity to solve the game.<br><br>" +
                                "<center>Have fun stepping on mines<br>" +
                                "or let the monkey do it for you :)</center>"+
                                "</html>", "Instructions", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        help_panel.add(help);



        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);


        // Set operation to close if
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {

                System.out.println("Saving game...");

                board.saveToFile();

                System.exit(0);

            }
        });



    }
}