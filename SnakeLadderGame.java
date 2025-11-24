// SnakeLadderGame.java
import javax.swing.*;

public class SnakeLadderGame extends JFrame {
    public SnakeLadderGame() {
        setTitle("Snake and Ladder 8x8 - Multiplayer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Show player selection dialog
        PlayerSelectionDialog dialog = new PlayerSelectionDialog(this);
        dialog.setVisible(true);

        int numPlayers = dialog.getSelectedPlayers();

        if (numPlayers > 0) {
            GamePanel panel = new GamePanel(numPlayers, dialog.getPlayerNames());
            add(panel);

            pack();
            setLocationRelativeTo(null);
            setResizable(false);
            setVisible(true);
        } else {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SnakeLadderGame());
    }
}