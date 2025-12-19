//// GameController.java
//import javax.swing.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.awt.Color;
//
//class GameController {
//    private Board board;
//    private List<Player> players;
//    private int currentPlayerIndex;
//    private Dice dice;
//    private ColorChoice colorChoice;
//    private GamePanel gamePanel;
//    private boolean isGameOver;
//    private int lastDiceRoll;
//    private int lastColorChoice;
//    private Player winner;
//    private boolean bonusTurn;
//
//    public GameController(GamePanel gamePanel, int numPlayers, List<String> playerNames) {
//        this.board = new Board();
//        this.players = new ArrayList<>();
//        this.dice = new Dice();
//        this.colorChoice = new ColorChoice();
//        this.gamePanel = gamePanel;
//        this.isGameOver = false;
//        this.lastDiceRoll = 0;
//        this.lastColorChoice = 0;
//        this.currentPlayerIndex = 0;
//        this.winner = null;
//        this.bonusTurn = false;
//
//        initializePlayers(numPlayers, playerNames);
//    }
//
//    private void initializePlayers(int numPlayers, List<String> playerNames) {
//        Color[] colors = {
//                new Color(30, 144, 255),   // Blue
//                new Color(220, 20, 60),    // Red
//                new Color(50, 205, 50),    // Green
//                new Color(255, 165, 0)     // Orange
//        };
//
//        for (int i = 0; i < numPlayers; i++) {
//            String name = (playerNames != null && i < playerNames.size())
//                    ? playerNames.get(i)
//                    : "Player " + (i + 1);
//            players.add(new Player(name, colors[i], i + 1));
//        }
//    }
//
//    // Helper: Cek apakah angka prima
//    private boolean isPrime(int number) {
//        if (number <= 1) return false;
//        for (int i = 2; i <= Math.sqrt(number); i++) {
//            if (number % i == 0) return false;
//        }
//        return true;
//    }
//
//    public void rollDice() {
//        // Hapus pengecekan waitingForChoice karena sudah tidak ada dialog tunggu
//        if (isGameOver || getCurrentPlayer().isMoving()) return;
//
//        lastDiceRoll = dice.roll();
//        lastColorChoice = colorChoice.getRandomChoice();
//
//        gamePanel.repaint();
//
//        // LANGSUNG JALAN TANPA DIALOG KONFIRMASI "GILIRAN ..."
//        executeMove();
//    }
//
//    private void executeMove() {
//        Player currentPlayer = getCurrentPlayer();
//        int currentPos = currentPlayer.getPosition();
//        int diceVal = lastDiceRoll;
//        int finalPos = currentPos;
//
//        // Logika Gerak
//        if (lastColorChoice == ColorChoice.GREEN) { // MAJU
//
//            // LOGIKA SPESIAL: Cek Prima
//            if (isPrime(currentPos)) {
//                boolean ladderFound = false;
//
//                for (int i = 1; i <= diceVal; i++) {
//                    int checkPos = currentPos + i;
//                    Ladder ladder = board.getLadderAt(checkPos);
//
//                    if (ladder != null) {
//                        // KETEMU TANGGA (INTERCEPT)
//                        int stepsUsed = i;
//                        int stepsRemaining = diceVal - stepsUsed;
//
//                        // Posisi langsung lompat ke: Puncak Tangga + Sisa Langkah
//                        finalPos = ladder.getEnd() + stepsRemaining;
//
//                        ladderFound = true;
//                        break;
//                    }
//                }
//
//                if (!ladderFound) {
//                    finalPos = currentPos + diceVal;
//                }
//
//            } else {
//                // Bukan Prima - Jalan Normal
//                finalPos = currentPos + diceVal;
//            }
//
//        } else {
//            // MUNDUR (MERAH)
//            finalPos = currentPos - diceVal;
//        }
//
//        // Validasi batas papan
//        if (finalPos < 1) finalPos = 1;
//        if (finalPos > board.getTotalCells()) finalPos = board.getTotalCells();
//
//        // Set tujuan akhir
//        currentPlayer.setTargetPosition(finalPos);
//        currentPlayer.setMoving(true);
//
//        // Cek Win Condition
//        if (finalPos == board.getTotalCells()) {
//            isGameOver = true;
//            winner = currentPlayer;
//        }
//
//        // Cek Bonus Turn (Kelipatan 5 pada posisi akhir)
//        if (finalPos % 5 == 0 && finalPos > 0 && finalPos < board.getTotalCells()) {
//            bonusTurn = true;
//        } else {
//            bonusTurn = false;
//        }
//
//        gamePanel.startAnimation();
//    }
//
//    public void updatePlayerPosition(int position) {
//        Player currentPlayer = getCurrentPlayer();
//        currentPlayer.setPosition(position);
//
//        // Jika animasi sampai di target
//        if (position == currentPlayer.getTargetPosition()) {
//            currentPlayer.setMoving(false);
//
//            if (isGameOver && winner != null) {
//                // Hanya pesan Game Over yang tersisa
//                JOptionPane.showMessageDialog(gamePanel,
//                        winner.getName() + " MENANG!",
//                        "Game Over",
//                        JOptionPane.INFORMATION_MESSAGE);
//            } else if (bonusTurn) {
//                // HAPUS PESAN POPUP BONUS
//                // Variable bonusTurn di-reset di sini agar logika tidak error,
//                // tapi kita TIDAK memanggil nextPlayer().
//                // Jadi user bisa langsung klik tombol Roll lagi.
//                bonusTurn = false;
//            } else {
//                // Ganti giliran ke pemain berikutnya
//                nextPlayer();
//            }
//        }
//    }
//
//    private void nextPlayer() {
//        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
//        lastDiceRoll = 0;
//        lastColorChoice = 0;
//        gamePanel.repaint();
//    }
//
//    public void resetGame() {
//        board = new Board();
//        for (Player player : players) {
//            player.setPosition(0);
//            player.setTargetPosition(0);
//            player.setMoving(false);
//        }
//        currentPlayerIndex = 0;
//        isGameOver = false;
//        lastDiceRoll = 0;
//        lastColorChoice = 0;
//        winner = null;
//        bonusTurn = false;
//        gamePanel.repaint();
//    }
//
//    public Board getBoard() { return board; }
//    public List<Player> getPlayers() { return players; }
//    public Player getCurrentPlayer() { return players.get(currentPlayerIndex); }
//    public Dice getDice() { return dice; }
//    public boolean isGameOver() { return isGameOver; }
//    public int getLastDiceRoll() { return lastDiceRoll; }
//    public int getLastColorChoice() { return lastColorChoice; }
//    // Hapus method isWaitingForChoice karena sudah tidak dipakai
//    public boolean isWaitingForChoice() { return false; }
//}