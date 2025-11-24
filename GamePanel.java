// GamePanel.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

class GamePanel extends JPanel {
    private GameController controller;
    private JButton rollButton;
    private JButton resetButton;
    private Timer animationTimer;
    private int animationStep;
    private static final int ANIMATION_STEPS = 15;

    public GamePanel(int numPlayers) {
        controller = new GameController(this, numPlayers);
        setPreferredSize(new Dimension(700, 750));
        setBackground(new Color(245, 245, 220));
        setLayout(null);

        setupButtons();
        setupAnimation();
    }

    private void setupButtons() {
        rollButton = new JButton("🎲 Roll Dice");
        rollButton.setBounds(200, 670, 140, 45);
        rollButton.setFont(new Font("Arial", Font.BOLD, 14));
        rollButton.setFocusPainted(false);
        rollButton.addActionListener(e -> controller.rollDice());
        add(rollButton);

        resetButton = new JButton("🔄 Reset Game");
        resetButton.setBounds(350, 670, 140, 45);
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetButton.setFocusPainted(false);
        resetButton.addActionListener(e -> controller.resetGame());
        add(resetButton);
    }

    private void setupAnimation() {
        animationTimer = new Timer(30, e -> {
            Player currentPlayer = controller.getCurrentPlayer();
            int currentPos = currentPlayer.getPosition();
            int targetPos = currentPlayer.getTargetPosition();

            if (currentPos < targetPos) {
                animationStep++;
                if (animationStep >= ANIMATION_STEPS) {
                    currentPos++;
                    controller.updatePlayerPosition(currentPos);
                    animationStep = 0;
                }
            } else if (currentPos > targetPos) {
                animationStep++;
                if (animationStep >= ANIMATION_STEPS) {
                    currentPos--;
                    controller.updatePlayerPosition(currentPos);
                    animationStep = 0;
                }
            } else {
                animationTimer.stop();
            }

            repaint();
        });
    }

    public void startAnimation() {
        animationStep = 0;
        animationTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        drawBoard(g2d);
        drawPlayers(g2d);
        drawInfo(g2d);
        drawPlayersList(g2d);
    }

    private void drawBoard(Graphics2D g2d) {
        Board board = controller.getBoard();

        for (Cell cell : board.getCells()) {
            int x = cell.getX();
            int y = cell.getY();
            int size = cell.getSize();

            // Alternating colors
            if ((cell.getNumber() + (cell.getNumber() - 1) / 8) % 2 == 0) {
                g2d.setColor(new Color(240, 230, 210));
            } else {
                g2d.setColor(new Color(210, 180, 140));
            }
            g2d.fillRect(x, y, size, size);

            // Border
            g2d.setColor(new Color(139, 69, 19));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(x, y, size, size);

            // Cell number
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            String numStr = String.valueOf(cell.getNumber());
            g2d.drawString(numStr, x + 10, y + 20);

            // Special cells
            if (cell.getNumber() == 1) {
                g2d.setColor(new Color(50, 205, 50, 100));
                g2d.fillRect(x, y, size, size);
                g2d.setColor(Color.BLACK);
                g2d.drawString("START", x + 10, y + size - 10);
            } else if (cell.getNumber() == 64) {
                g2d.setColor(new Color(255, 215, 0, 100));
                g2d.fillRect(x, y, size, size);
                g2d.setColor(Color.BLACK);
                g2d.drawString("FINISH", x + 5, y + size - 10);
            }
        }
    }

    private void drawPlayers(Graphics2D g2d) {
        List<Player> players = controller.getPlayers();

        for (Player player : players) {
            int position = player.getPosition();
            if (position < 1) continue;

            Cell cell = controller.getBoard().getCell(position);

            // Calculate offset for multiple players on same cell
            int playerIndex = players.indexOf(player);
            int totalPlayers = players.size();
            int offsetX = 0;
            int offsetY = 0;

            if (totalPlayers == 2) {
                offsetX = (playerIndex == 0) ? -12 : 12;
            } else if (totalPlayers == 3) {
                offsetX = (playerIndex == 0) ? -15 : (playerIndex == 1) ? 0 : 15;
            } else if (totalPlayers == 4) {
                offsetX = (playerIndex % 2 == 0) ? -12 : 12;
                offsetY = (playerIndex < 2) ? -12 : 12;
            }

            int x = cell.getCenterX() + offsetX;
            int y = cell.getCenterY() + offsetY;

            // Draw player shadow
            g2d.setColor(new Color(0, 0, 0, 50));
            g2d.fillOval(x - 13, y - 11, 26, 26);

            // Draw player
            g2d.setColor(player.getColor());
            g2d.fillOval(x - 15, y - 15, 30, 30);

            // Draw player border
            if (player == controller.getCurrentPlayer() && !controller.isGameOver()) {
                g2d.setColor(new Color(255, 215, 0));
                g2d.setStroke(new BasicStroke(3));
            } else {
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(2));
            }
            g2d.drawOval(x - 15, y - 15, 30, 30);

            // Draw player number
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            String pNum = String.valueOf(player.getPlayerNumber());
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(pNum);
            g2d.drawString(pNum, x - textWidth/2, y + 5);
        }
    }

    private void drawInfo(Graphics2D g2d) {
        // Current turn info
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRoundRect(50, 10, 600, 60, 15, 15);

        Player currentPlayer = controller.getCurrentPlayer();
        g2d.setColor(currentPlayer.getColor());
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("Giliran: " + currentPlayer.getName(), 70, 35);

        g2d.setColor(Color.WHITE);
        g2d.drawString("Posisi: " + currentPlayer.getPosition(), 250, 35);

        if (controller.isWaitingForChoice() || controller.getLastDiceRoll() > 0) {
            g2d.drawString("Dadu: " + controller.getLastDiceRoll(), 380, 35);

            String colorText = controller.getLastColorChoice() == ColorChoice.GREEN ?
                    "HIJAU" : "MERAH";
            Color colorDisplay = controller.getLastColorChoice() == ColorChoice.GREEN ?
                    new Color(50, 205, 50) : new Color(220, 20, 60);

            g2d.setColor(colorDisplay);
            g2d.drawString(colorText, 500, 35);
        }

        // Instructions
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        g2d.drawString("Hijau (80%): Maju | Merah (20%): Mundur", 70, 55);
    }

    private void drawPlayersList(Graphics2D g2d) {
        List<Player> players = controller.getPlayers();

        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRoundRect(50, 640, 600, 25, 10, 10);

        int startX = 70;
        for (Player player : players) {
            // Player color indicator
            g2d.setColor(player.getColor());
            g2d.fillOval(startX, 647, 12, 12);

            // Player info
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.PLAIN, 11));
            g2d.drawString(player.getName() + ": " + player.getPosition(), startX + 18, 658);

            startX += 140;
        }
    }
}