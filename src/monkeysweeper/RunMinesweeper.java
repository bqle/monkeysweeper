package monkeysweeper;
/**
 * CIS 120 HW09 - Minesweeper
 */

import javax.swing.*;
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
 * In a Model-View-Controller framework, minesweeper.Game initializes the view,
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

        JButton[] buttons = new JButton[] { reset,
            solveLikePro, solveLikeAmateur, solveLikeMonkey, save, help };

        // minesweeper.Game board
        final GameBoard board = new GameBoard(status, frame, buttons);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.reset();
            }
        });
        control_panel.add(reset);

        // SolveLikeMonkey button
        solveLikeMonkey.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.solveLikeMonkey();
            }
        });
        control_panel.add(solveLikeMonkey);

        // SolveLikeAmateur button
        solveLikeAmateur.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.solveLikeAmateur();
            }
        });
        control_panel.add(solveLikeAmateur);

        // SolveLikePro button
        solveLikePro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.solveLikePro();
            }
        });
        control_panel.add(solveLikePro);

        // Save button
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.saveToFile(true);
            }
        });
        help_panel.add(save);

        // Help button
        help.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                        frame,
                        "<html><center>Welcome to <i>Minesweeper V2</i></center><br>" +
                                "Left click to reveal a square<br>" +
                                "Right click to place or remove a flag<br>" +
                                "(sorry, Mac users, but the game doesn't have trackpad support)<br>"
                                +
                                "The side options come with abilities<br>" +
                                "of increasing dexterity to solve the game.<br><br>" +
                                "<center>Have fun stepping on mines<br>" +
                                "or let the monkey do it for you :)</center>" +
                                "</html>",
                        "Instructions", JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        help_panel.add(help);

        // Put the frame on the screen
        frame.pack();
        frame.setVisible(true);

        // Set operation to close if
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int confirm = JOptionPane.showOptionDialog(
                        frame,
                        "Would you like to save the game?",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null
                );

                // There are other options as well
                if (confirm == JOptionPane.NO_OPTION) {
                    board.reset();
                    board.saveToFile(false);
                    System.exit(1);
                } else if (confirm == JOptionPane.YES_OPTION) {
                    board.saveToFile(true);
                    System.exit(1);
                }

            }
        });

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    }
}