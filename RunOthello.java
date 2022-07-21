import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RunOthello implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Othello");
        frame.setLocation(800, 800);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Game board
        final JLabel blackCount = new JLabel("Player 1 (Black): " + 2);
        final JLabel whiteCount = new JLabel("Player 2 (White // AI): " + 2);
        final JButton instructions = new JButton("Instructions");

        String instructMessage = "<html><br>Hello! This game is Othello. " +
                "The exact rules can be found here: https://www.fgbradleys.com/rules/Othello.pdf."
                +
                "<br>Black goes first, as is standard. " +
                "During a turn, black clicks on the next square they want their piece to go."
                +
                "<br>White, which is an AI that uses the minimax algorithm, will then move."
                +
                "<br>The board will update accordingly, and any invalid moves won't be registered. "
                +
                "<br>When neither player has any valid moves left, " +
                "the game is over and whoever has the most pieces wins. Good luck!</html>";
        instructions.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    frame, instructMessage, "Instructions", JOptionPane.PLAIN_MESSAGE
            );
        });

        final OGameBoard board = new OGameBoard(status, blackCount, whiteCount);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.reset();
            }
        });

        control_panel.add(instructions);
        control_panel.add(blackCount);
        control_panel.add(reset);
        control_panel.add(whiteCount);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        // board.reset();
    }
}