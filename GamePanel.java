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

    public GamePanel(int numPlayers, List<String> playerNames) {
        controller = new GameController(this, numPlayers, playerNames);
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
        drawLadders(g2d);
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
            int cellNum = cell.getNumber();

            // Alternating colors
            if ((cellNum + (cellNum - 1) / 8) % 2 == 0) {
                g2d.setColor(new Color(240, 230, 210));
            } else {
                g2d.setColor(new Color(210, 180, 140));
            }
            g2d.fillRect(x, y, size, size);

            // Highlight kelipatan 5 (bonus cells)
            if (cellNum % 5 == 0 && cellNum > 0 && cellNum < 64) {
                g2d.setColor(new Color(135, 206, 250, 100)); // Light blue overlay
                g2d.fillRect(x, y, size, size);
            }

            // Border
            g2d.setColor(new Color(139, 69, 19));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(x, y, size, size);

            // Cell number
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            String numStr = String.valueOf(cellNum);
            g2d.drawString(numStr, x + 10, y + 20);

            // Special cells
            if (cellNum == 1) {
                g2d.setColor(new Color(50, 205, 50, 100));
                g2d.fillRect(x, y, size, size);
                g2d.setColor(Color.BLACK);
                g2d.drawString("START", x + 10, y + size - 10);
            } else if (cellNum == 64) {
                g2d.setColor(new Color(255, 215, 0, 100));
                g2d.fillRect(x, y, size, size);
                g2d.setColor(Color.BLACK);
                g2d.drawString("FINISH", x + 5, y + size - 10);
            } else if (cellNum % 5 == 0) {
                // Draw star icon for bonus cells
                g2d.setColor(new Color(255, 215, 0));
                g2d.setFont(new Font("Arial", Font.BOLD, 20));
                g2d.drawString("⭐", x + size - 25, y + size - 5);
            }
        }
    }

    private void drawLadders(Graphics2D g2d) {
        Board board = controller.getBoard();
        List<Ladder> ladders = board.getLadders();

        for (Ladder ladder : ladders) {
            Cell startCell = board.getCell(ladder.getStart());
            Cell endCell = board.getCell(ladder.getEnd());

            int x1 = startCell.getCenterX();
            int y1 = startCell.getCenterY();
            int x2 = endCell.getCenterX();
            int y2 = endCell.getCenterY();

            // Draw ladder line (two parallel lines for side rails)
            g2d.setColor(new Color(139, 69, 19));
            g2d.setStroke(new BasicStroke(4));

            // Calculate perpendicular offset for parallel lines
            double angle = Math.atan2(y2 - y1, x2 - x1);
            int offsetDist = 6;
            int offsetX = (int)(Math.sin(angle) * offsetDist);
            int offsetY = (int)(-Math.cos(angle) * offsetDist);

            // Draw two side rails
            g2d.drawLine(x1 - offsetX, y1 - offsetY, x2 - offsetX, y2 - offsetY);
            g2d.drawLine(x1 + offsetX, y1 + offsetY, x2 + offsetX, y2 + offsetY);

            // Draw ladder rungs
            g2d.setStroke(new BasicStroke(3));
            int numRungs = 5;
            for (int i = 1; i <= numRungs; i++) {
                int rx = x1 + (x2 - x1) * i / (numRungs + 1);
                int ry = y1 + (y2 - y1) * i / (numRungs + 1);

                g2d.drawLine(rx - offsetX, ry - offsetY, rx + offsetX, ry + offsetY);
            }

            // Draw arrow at end
            g2d.setColor(new Color(34, 139, 34));
            g2d.setStroke(new BasicStroke(3));
            drawArrow(g2d, x1, y1, x2, y2);

            // Draw ladder emoji
            g2d.setColor(new Color(139, 69, 19));
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            int midX = (x1 + x2) / 2;
            int midY = (y1 + y2) / 2;

            // Draw white background for emoji
            g2d.setColor(new Color(255, 255, 255, 200));
            g2d.fillOval(midX - 12, midY - 12, 24, 24);

            g2d.setColor(new Color(139, 69, 19));
            g2d.drawString("🪜", midX - 8, midY + 6);
        }
    }

    private void drawArrow(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        int arrowSize = 15;

        int[] xPoints = new int[3];
        int[] yPoints = new int[3];

        xPoints[0] = x2;
        yPoints[0] = y2;

        xPoints[1] = (int)(x2 - arrowSize * Math.cos(angle - Math.PI / 6));
        yPoints[1] = (int)(y2 - arrowSize * Math.sin(angle - Math.PI / 6));

        xPoints[2] = (int)(x2 - arrowSize * Math.cos(angle + Math.PI / 6));
        yPoints[2] = (int)(y2 - arrowSize * Math.sin(angle + Math.PI / 6));

        g2d.fillPolygon(xPoints, yPoints, 3);
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
        g2d.drawString("Posisi: " + currentPlayer.getPosition(), 280, 35);

        if (controller.isWaitingForChoice() || controller.getLastDiceRoll() > 0) {
            g2d.drawString("Dadu: " + controller.getLastDiceRoll(), 400, 35);

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
        g2d.drawString("Hijau (80%): Maju | Merah (20%): Mundur | ⭐ Bonus | 🪜 Tangga", 70, 55);
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