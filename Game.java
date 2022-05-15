import javax.swing.*;

public class Game {
    public static void main(String[] args) {
        Runnable game = new RunOthello(); //Sets the game you want to run.
        SwingUtilities.invokeLater(game);
    }
}
