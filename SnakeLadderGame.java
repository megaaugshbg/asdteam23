// SnakeLadderGame.java
import javax.swing.*;

public class SnakeLadderGame extends JFrame {
    public SnakeLadderGame() {
        setTitle("Snake and Ladder 8x8 - Dice Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePanel panel = new GamePanel();
        add(panel);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SnakeLadderGame());
    }
}