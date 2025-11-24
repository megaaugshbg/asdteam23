// GameController.java
import javax.swing.*;

class GameController {
    private Board board;
    private Player player;
    private Dice dice;
    private ColorChoice colorChoice;
    private GamePanel gamePanel;
    private boolean isGameOver;
    private int lastDiceRoll;
    private int lastColorChoice;
    private boolean waitingForChoice;

    public GameController(GamePanel gamePanel) {
        this.board = new Board();
        this.player = new Player("Player 1", new java.awt.Color(30, 144, 255));
        this.dice = new Dice();
        this.colorChoice = new ColorChoice();
        this.gamePanel = gamePanel;
        this.isGameOver = false;
        this.waitingForChoice = false;
        this.lastDiceRoll = 0;
        this.lastColorChoice = 0;
    }

    public void rollDice() {
        if (isGameOver || player.isMoving() || waitingForChoice) return;

        lastDiceRoll = dice.roll();
        lastColorChoice = colorChoice.getRandomChoice();
        waitingForChoice = true;

        gamePanel.repaint();

        // Show choice dialog
        String message = String.format(
                "Dadu: %d\nWarna: %s\n\nApakah Anda ingin bergerak?",
                lastDiceRoll,
                colorChoice.getColorName(lastColorChoice)
        );

        int choice = JOptionPane.showConfirmDialog(
                gamePanel,
                message,
                "Hasil Lemparan",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            executeMove();
        } else {
            waitingForChoice = false;
            gamePanel.repaint();
        }
    }

    private void executeMove() {
        int currentPos = player.getPosition();
        int newPos;

        if (lastColorChoice == ColorChoice.GREEN) {
            // Hijau: maju
            newPos = currentPos + lastDiceRoll;
        } else {
            // Merah: mundur
            newPos = currentPos - lastDiceRoll;
        }

        // Validasi posisi
        if (newPos < 1) {
            newPos = 1;
        } else if (newPos > board.getTotalCells()) {
            newPos = board.getTotalCells();
        }

        player.setTargetPosition(newPos);
        player.setMoving(true);
        waitingForChoice = false;

        // Check win condition
        if (newPos == board.getTotalCells()) {
            isGameOver = true;
        }

        gamePanel.startAnimation();
    }

    public void updatePlayerPosition(int position) {
        player.setPosition(position);
        if (position == player.getTargetPosition()) {
            player.setMoving(false);
            if (isGameOver) {
                JOptionPane.showMessageDialog(gamePanel,
                        "Selamat! Anda mencapai kotak 64!",
                        "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public void resetGame() {
        player.setPosition(0);
        player.setTargetPosition(0);
        player.setMoving(false);
        isGameOver = false;
        waitingForChoice = false;
        lastDiceRoll = 0;
        lastColorChoice = 0;
        gamePanel.repaint();
    }

    public Board getBoard() { return board; }
    public Player getPlayer() { return player; }
    public Dice getDice() { return dice; }
    public boolean isGameOver() { return isGameOver; }
    public int getLastDiceRoll() { return lastDiceRoll; }
    public int getLastColorChoice() { return lastColorChoice; }
    public boolean isWaitingForChoice() { return waitingForChoice; }
}
