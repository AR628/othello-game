import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class OGameBoard extends JPanel {

    private Othello o; // model for the game
    private JLabel status; // current status text
    private JLabel blackCounter;
    private JLabel whiteCounter;
    private FileWriter fw;

    // Game constants
    public static final int BOARD_WIDTH = 800;
    public static final int BOARD_HEIGHT = 800;

    /**
     * Initializes the game board.
     */
    public OGameBoard(JLabel statusInit, JLabel blackCounter, JLabel whiteCounter) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        o = new Othello(); // initializes model for the game
        status = statusInit; // initializes the status JLabel
        this.blackCounter = blackCounter;
        this.whiteCounter = whiteCounter;

        updateGameState();
        updateStatus();
        repaint();

        try {
            fw = new FileWriter("GameState.txt", true);
        } catch (IOException e) {

        }
        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();

                // updates the model given the coordinates of the mouseclick
                boolean successful = o.playTurn(p.y / 100, p.x / 100);
                if (successful) {
                    try {
                        fw.write(p.y / 100 + "" + p.x / 100 + "\n");
                        fw.flush();
                    } catch (IOException e1) {

                    }
                }
                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        // Block of code needed here just to clear the file.
        try {
            new FileWriter("GameState.txt", false);
        } catch (IOException e) {

        }

        try {
            fw = new FileWriter("GameState.txt", true);
        } catch (IOException e) {

        }
        o.reset();
        status.setText("Player 1's Turn");
        blackCounter.setText("Player 1 (Black): " + 2);
        whiteCounter.setText("Player 2 (White): " + 2);
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (o.getCurrentPlayer()) {
            status.setText("Player 1's Turn");
        } else {
            status.setText("Player 2's Turn");
        }

        blackCounter.setText("Player 1 (Black): " + o.getBlackPieces());
        whiteCounter.setText("Player 2 (White): " + o.getWhitePieces());

        int winner = o.checkWinner();
        if (winner == 1) {
            status.setText("Player 1 wins!!!");
        } else if (winner == 2) {
            status.setText("Player 2 wins!!!");
        } else if (winner == 3) {
            status.setText("It's a tie.");
        }
    }

    /**
     * Draws the game board.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int size = BOARD_WIDTH;
        // Draws board grid
        for (int a = 0; a < 8; a++) {
            g.drawLine(a * (size / 8), 0, a * (size / 8), size);
            g.drawLine(0, a * (size / 8), size, a * (size / 8));
        }

        // Draws X's and O's
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int state = o.getCell(i, j);
                if (state == 1) {
                    g.setColor(Color.black);
                    g.fillOval(30 + 100 * j, 30 + 100 * i, 40, 40);
                } else if (state == 2) {
                    g.setColor(Color.white);
                    g.fillOval(30 + 100 * j, 30 + 100 * i, 40, 40);
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

    public void save() {

    }

    /**
     * Updates game state from the local file "GameState.txt".
     */
    public void updateGameState() {
        BufferedReader br;
        try {
            br = new BufferedReader(
                    new FileReader("GameState.txt")
            );
            FileLineIterator li = new FileLineIterator(br);
            while (li.hasNext()) {
                int turn = Integer.valueOf(li.next());
                int r = turn / 10;
                int c = turn % 10;
                o.playTurn(r, c);
            }
        } catch (FileNotFoundException e) {

        }
    }
}
