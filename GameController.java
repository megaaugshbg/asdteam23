// GameController.java
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

class GameController {
    private Board board;
    private List<Player> players;
    private int currentPlayerIndex;
    private Dice dice;
    private ColorChoice colorChoice;
    private GamePanel gamePanel;
    private boolean isGameOver;
    private int lastDiceRoll;
    private int lastColorChoice;
    private boolean waitingForChoice;
    private Player winner;

    public GameController(GamePanel gamePanel, int numPlayers) {
        this.board = new Board();
        this.players = new ArrayList<>();
        this.dice = new Dice();
        this.colorChoice = new ColorChoice();
        this.gamePanel = gamePanel;
        this.isGameOver = false;
        this.waitingForChoice = false;
        this.lastDiceRoll = 0;
        this.lastColorChoice = 0;
        this.currentPlayerIndex = 0;
        this.winner = null;

        initializePlayers(numPlayers);
    }

    private void initializePlayers(int numPlayers) {
        Color[] colors = {
                new Color(30, 144, 255),   // Blue
                new Color(220, 20, 60),     // Red
                new Color(50, 205, 50),     // Green
                new Color(255, 165, 0)      // Orange
        };

        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player("Player " + (i + 1), colors[i], i + 1));
        }
    }

    public void rollDice() {
        if (isGameOver || getCurrentPlayer().isMoving() || waitingForChoice) return;

        lastDiceRoll = dice.roll();
        lastColorChoice = colorChoice.getRandomChoice();
        waitingForChoice = true;

        gamePanel.repaint();

        // Show choice dialog
        String message = String.format(
                "%s\n\nDadu: %d\nWarna: %s\n\nApakah Anda ingin bergerak?",
                getCurrentPlayer().getName(),
                lastDiceRoll,
                colorChoice.getColorName(lastColorChoice)
        );

        int choice = JOptionPane.showConfirmDialog(
                gamePanel,
                message,
                "Giliran " + getCurrentPlayer().getName(),
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            executeMove();
        } else {
            waitingForChoice = false;
            nextPlayer();
            gamePanel.repaint();
        }
    }

    private void executeMove() {
        Player currentPlayer = getCurrentPlayer();
        int currentPos = currentPlayer.getPosition();
        int newPos;

        if (lastColorChoice == ColorChoice.GREEN) {
            newPos = currentPos + lastDiceRoll;
        } else {
            newPos = currentPos - lastDiceRoll;
        }

        // Validasi posisi
        if (newPos < 1) {
            newPos = 1;
        } else if (newPos > board.getTotalCells()) {
            newPos = board.getTotalCells();
        }

        currentPlayer.setTargetPosition(newPos);
        currentPlayer.setMoving(true);
        waitingForChoice = false;

        // Check win condition
        if (newPos == board.getTotalCells()) {
            isGameOver = true;
            winner = currentPlayer;
        }

        gamePanel.startAnimation();
    }

    public void updatePlayerPosition(int position) {
        Player currentPlayer = getCurrentPlayer();
        currentPlayer.setPosition(position);

        if (position == currentPlayer.getTargetPosition()) {
            currentPlayer.setMoving(false);

            if (isGameOver && winner != null) {
                JOptionPane.showMessageDialog(gamePanel,
                        winner.getName() + " MENANG!\n\nSelamat mencapai kotak 64!",
                        "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                nextPlayer();
            }
        }
    }

    private void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        lastDiceRoll = 0;
        lastColorChoice = 0;
        gamePanel.repaint();
    }

    public void resetGame() {
        for (Player player : players) {
            player.setPosition(0);
            player.setTargetPosition(0);
            player.setMoving(false);
        }
        currentPlayerIndex = 0;
        isGameOver = false;
        waitingForChoice = false;
        lastDiceRoll = 0;
        lastColorChoice = 0;
        winner = null;
        gamePanel.repaint();
    }

    public Board getBoard() { return board; }
    public List<Player> getPlayers() { return players; }
    public Player getCurrentPlayer() { return players.get(currentPlayerIndex); }
    public Dice getDice() { return dice; }
    public boolean isGameOver() { return isGameOver; }
    public int getLastDiceRoll() { return lastDiceRoll; }
    public int getLastColorChoice() { return lastColorChoice; }
    public boolean isWaitingForChoice() { return waitingForChoice; }
}