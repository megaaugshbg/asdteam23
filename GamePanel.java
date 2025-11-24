// GamePanel.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class GamePanel extends JPanel {
    private GameController controller;
    private JButton rollButton;
    private JButton resetButton;
    private Timer animationTimer;
    private int animationStep;
    private static final int ANIMATION_STEPS = 20;

    public GamePanel() {
        controller = new GameController(this);
        setPreferredSize(new Dimension(700, 700));
        setBackground(new Color(245, 245, 220));
        setLayout(null);

        setupButtons();
        setupAnimation();
    }

    private void setupButtons() {
        rollButton = new JButton("🎲 Roll Dice");
        rollButton.setBounds(250, 620, 120, 40);
        rollButton.setFont(new Font("Arial", Font.BOLD, 14));
        rollButton.setFocusPainted(false);
        rollButton.addActionListener(e -> controller.rollDice());
        add(rollButton);

        resetButton = new JButton("🔄 Reset");
        resetButton.setBounds(380, 620, 100, 40);
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetButton.setFocusPainted(false);
        resetButton.addActionListener(e -> controller.resetGame());
        add(resetButton);
    }

    private void setupAnimation() {
        animationTimer = new Timer(30, e -> {
            Player player = controller.getPlayer();
            int currentPos = player.getPosition();
            int targetPos = player.getTargetPosition();

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
        drawPlayer(g2d);
        drawInfo(g2d);
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
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(numStr);
            int textHeight = fm.getAscent();
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

    private void drawPlayer(Graphics2D g2d) {
        Player player = controller.getPlayer();
        int position = player.getPosition();

        if (position < 1) return;

        Cell cell = controller.getBoard().getCell(position);
        int x = cell.getCenterX();
        int y = cell.getCenterY();

        // Draw player shadow
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.fillOval(x - 18, y - 16, 36, 36);

        // Draw player
        g2d.setColor(player.getColor());
        g2d.fillOval(x - 20, y - 20, 40, 40);

        // Draw player border
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval(x - 20, y - 20, 40, 40);

        // Draw player number
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("P", x - 7, y + 6);
    }

    private void drawInfo(Graphics2D g2d) {
        // Info panel
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRoundRect(50, 10, 600, 60, 15, 15);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString("Position: " + controller.getPlayer().getPosition(), 70, 35);

        if (controller.isWaitingForChoice() || controller.getLastDiceRoll() > 0) {
            g2d.drawString("Dice: " + controller.getLastDiceRoll(), 220, 35);

            String colorText = controller.getLastColorChoice() == ColorChoice.GREEN ?
                    "Color: HIJAU (Maju)" : "Color: MERAH (Mundur)";
            Color colorDisplay = controller.getLastColorChoice() == ColorChoice.GREEN ?
                    new Color(50, 205, 50) : new Color(220, 20, 60);

            g2d.setColor(colorDisplay);
            g2d.drawString(colorText, 320, 35);
        }

        // Instructions
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString("Hijau (80%): Maju | Merah (20%): Mundur", 70, 55);
    }
}
