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
    private boolean bonusTurn;

    public GameController(GamePanel gamePanel, int numPlayers, List<String> playerNames) {
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
        this.bonusTurn = false;

        initializePlayers(numPlayers, playerNames);
    }

    private void initializePlayers(int numPlayers, List<String> playerNames) {
        Color[] colors = {
                new Color(30, 144, 255),   // Blue
                new Color(220, 20, 60),     // Red
                new Color(50, 205, 50),     // Green
                new Color(255, 165, 0)      // Orange
        };

        for (int i = 0; i < numPlayers; i++) {
            String name = (playerNames != null && i < playerNames.size())
                    ? playerNames.get(i)
                    : "Player " + (i + 1);
            players.add(new Player(name, colors[i], i + 1));
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
            bonusTurn = false;
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

        // Check bonus turn (kelipatan 5)
        if (newPos % 5 == 0 && newPos > 0 && newPos < board.getTotalCells()) {
            bonusTurn = true;
        } else {
            bonusTurn = false;
        }

        gamePanel.startAnimation();
    }

    public void updatePlayerPosition(int position) {
        Player currentPlayer = getCurrentPlayer();
        currentPlayer.setPosition(position);

        if (position == currentPlayer.getTargetPosition()) {
            currentPlayer.setMoving(false);

            // Check for ladder
            Ladder ladder = board.getLadderAt(position);
            if (ladder != null && !isGameOver) {
                JOptionPane.showMessageDialog(gamePanel,
                        "🪜 TANGGA! 🪜\n\n" + currentPlayer.getName() +
                                " naik tangga dari kotak " + ladder.getStart() +
                                " ke kotak " + ladder.getEnd() + "!",
                        "Tangga",
                        JOptionPane.INFORMATION_MESSAGE);

                // Move to ladder end
                currentPlayer.setPosition(ladder.getEnd());
                currentPlayer.setTargetPosition(ladder.getEnd());

                // Check if reached finish via ladder
                if (ladder.getEnd() == board.getTotalCells()) {
                    isGameOver = true;
                    winner = currentPlayer;
                }

                // Reset bonus turn if ladder moves player
                bonusTurn = false;

                gamePanel.repaint();
            }

            if (isGameOver && winner != null) {
                JOptionPane.showMessageDialog(gamePanel,
                        winner.getName() + " MENANG!\n\nSelamat mencapai kotak 64!",
                        "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
            } else if (bonusTurn) {
                // Show bonus turn message
                JOptionPane.showMessageDialog(gamePanel,
                        "🎉 BONUS! 🎉\n\n" + currentPlayer.getName() +
                                " mendarat di kotak " + position + " (kelipatan 5)!\n" +
                                "Anda mendapat giliran ROLL DICE lagi!",
                        "Bonus Turn",
                        JOptionPane.INFORMATION_MESSAGE);
                bonusTurn = false;
                // Tidak next player, giliran tetap di player ini
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
        // Generate tangga baru
        board = new Board();

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
        bonusTurn = false;
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