package main;

import logic.GameController;
import logic.PathFinder;
import model.Dice;
import model.GameMap;
import model.Node;
import model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class GamePanel extends JPanel {

    private final GameFrame frame;
    private final GameMap gameMap;
    private final List<Player> players;
    private int currentPlayerIndex = 0;
    private final Image mapImage;

    // UI Components
    private JButton rollDiceBtn;
    private JButton backBtn;
    private JLabel statusLabel;
    private JTextArea scoreArea;

    private static final int MAX_NODE_ID = 64;

    public GamePanel(GameFrame frame, int numPlayers) {
        this.frame = frame;
        this.gameMap = new GameMap();

        setLayout(null);
        setBackground(Color.WHITE);

        players = initializePlayers(numPlayers);

        // Load map image
        java.net.URL imageURL = getClass().getResource("/Asset/map.png");
        if (imageURL != null) {
            mapImage = new ImageIcon(imageURL).getImage();
        } else {
            System.err.println("Warning: Map image not found at /Asset/map.png");
            mapImage = null;
        }

        setupGameUI();

        // Cek Prime Rule untuk player pertama
        GameController.checkPrimeRule(players.get(0), gameMap);
        updateStatus();
    }

    private List<Player> initializePlayers(int count) {
        List<Player> pList = new ArrayList<>();
        Color[] colors = {
                new Color(30, 144, 255),   // Blue
                new Color(220, 20, 60),    // Red
                new Color(50, 205, 50),    // Green
                new Color(255, 140, 0)     // Orange
        };
        for (int i = 0; i < count; i++) {
            pList.add(new Player("Player " + (i + 1), colors[i % colors.length]));
        }
        return pList;
    }

    private void setupGameUI() {
        backBtn = new JButton("🏠 Back");
        backBtn.setBounds(20, 20, 120, 35);
        backBtn.setFont(new Font("Arial", Font.BOLD, 14));
        backBtn.setBackground(new Color(244, 67, 54));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to quit?",
                    "Quit Game",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                frame.showDashboard();
            }
        });
        add(backBtn);

        // Status Panel
        JPanel statusPanel = new JPanel();
        statusPanel.setBounds(750, 30, 230, 100);
        statusPanel.setBackground(new Color(102, 126, 234));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusPanel.add(Box.createVerticalStrut(10));
        statusPanel.add(statusLabel);
        add(statusPanel);

        // Score Area
        JLabel scoreTitle = new JLabel("📊 SCOREBOARD");
        scoreTitle.setBounds(750, 150, 230, 25);
        scoreTitle.setFont(new Font("Arial", Font.BOLD, 14));
        scoreTitle.setOpaque(true);
        scoreTitle.setBackground(new Color(102, 126, 234));
        scoreTitle.setForeground(Color.WHITE);
        scoreTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(scoreTitle);

        scoreArea = new JTextArea();
        scoreArea.setBounds(750, 175, 230, 150);
        scoreArea.setEditable(false);
        scoreArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        scoreArea.setBackground(new Color(240, 240, 240));
        scoreArea.setBorder(BorderFactory.createLineBorder(new Color(102, 126, 234), 2));
        add(scoreArea);

        // Dice Buttons
        rollDiceBtn = new JButton("🎲 Roll Green Dice");
        rollDiceBtn.setBounds(750, 350, 230, 45);
        rollDiceBtn.setFont(new Font("Arial", Font.BOLD, 14));
        rollDiceBtn.setBackground(new Color(76, 175, 80));
        rollDiceBtn.setForeground(Color.WHITE);
        rollDiceBtn.setFocusPainted(false);
        rollDiceBtn.addActionListener(e -> rollGreenDice());
        add(rollDiceBtn);

        JButton redDiceBtn = new JButton("🎲 Roll Red Dice");
        redDiceBtn.setBounds(750, 405, 230, 45);
        redDiceBtn.setFont(new Font("Arial", Font.BOLD, 14));
        redDiceBtn.setBackground(new Color(244, 67, 54));
        redDiceBtn.setForeground(Color.WHITE);
        redDiceBtn.setFocusPainted(false);
        redDiceBtn.addActionListener(e -> rollRedDice());
        add(redDiceBtn);

        // Legend
        JLabel legend = new JLabel("<html>⭐ Star Node = Bonus Turn<br>🏁 = Finish<br>Coin = +10 Score</html>");
        legend.setBounds(750, 480, 230, 80);
        legend.setFont(new Font("Arial", Font.PLAIN, 11));
        legend.setVerticalAlignment(SwingConstants.TOP);
        legend.setBorder(BorderFactory.createTitledBorder("Legend"));
        add(legend);
    }

    private void updateStatus() {
        Player p = players.get(currentPlayerIndex);
        statusLabel.setText(p.name + "'s Turn");

        StringBuilder sb = new StringBuilder();
        for (Player player : players) {
            sb.append(String.format("%-10s", player.name))
                    .append(" | Score: ").append(String.format("%3d", player.score))
                    .append(" | Node: ").append(String.format("%2d", player.currentNode));

            if (player.shortestPathActive) {
                sb.append(" 🔥");
            }
            sb.append("\n");
        }
        scoreArea.setText(sb.toString());

        repaint();
    }

    private void rollGreenDice() {
        Player p = players.get(currentPlayerIndex);
        int steps = Dice.rollGreen();

        JOptionPane.showMessageDialog(this,
                p.name + " rolled " + steps + "!\n🎲 Green Dice - Move Forward",
                "Dice Roll",
                JOptionPane.INFORMATION_MESSAGE);

        if (p.shortestPathActive) {
            applyShortestPath(p, steps);
            p.shortestPathActive = false;
        } else {
            moveNormal(p, steps);
        }

        checkWinCondition(p);

        Node currentNode = gameMap.getNodeById(p.currentNode);

        if (currentNode != null && currentNode.isStar) {
            JOptionPane.showMessageDialog(this,
                    "⭐ " + p.name + " landed on a STAR NODE!\n🎉 Roll again!",
                    "Bonus Turn!",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            nextTurn();
        }
        updateStatus();
    }

    private void applyShortestPath(Player p, int steps) {
        Node start = gameMap.getNodeById(p.currentNode);
        List<Node> path = PathFinder.dijkstra(start, gameMap.finishNode);

        if (path.size() > 1 && path.get(0).equals(start)) {
            int moveIndex = Math.min(steps, path.size() - 1);
            Node next = path.get(moveIndex);

            if (next.id > p.currentNode || next.id == gameMap.finishNode.id) {
                GameController.movePlayer(p, next, gameMap);
                JOptionPane.showMessageDialog(this,
                        "🔥 Prime Rule Activated!\n" + p.name + " used Shortest Path to Node " + next.id,
                        "Prime Power!",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                moveNormal(p, steps);
            }
        } else {
            moveNormal(p, steps);
        }
    }

    private void moveNormal(Player p, int steps) {
        Node current = gameMap.getNodeById(p.currentNode);
        Node nextNode = findNextNode(current, steps);

        if (nextNode != null && nextNode.id <= MAX_NODE_ID) {
            GameController.movePlayer(p, nextNode, gameMap);
        } else if (nextNode != null && nextNode.id > MAX_NODE_ID) {
            GameController.movePlayer(p, gameMap.finishNode, gameMap);
        }
    }

    private Node findNextNode(Node start, int steps) {
        Node current = start;
        for (int i = 0; i < steps; i++) {
            final Node iterationCurrent = current;

            Optional<Node> nextOpt = current.neighbors.stream()
                    .filter(n -> n.id > iterationCurrent.id)
                    .max(Comparator.comparingInt(n -> n.id));

            Node next = nextOpt.orElse(
                    current.neighbors.stream()
                            .filter(n -> !n.equals(iterationCurrent))
                            .findFirst()
                            .orElse(null)
            );

            if (next == null || next.equals(current) || next.id > MAX_NODE_ID) break;

            current = next;
        }
        return current;
    }

    private void rollRedDice() {
        Player p = players.get(currentPlayerIndex);
        boolean isRed = Dice.rollRed();

        if (isRed) {
            GameController.redDiceEffect(p);
            JOptionPane.showMessageDialog(this,
                    "🎲 " + p.name + " rolled RED!\n⬅️ Back to Node " + p.currentNode,
                    "Red Dice Effect",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "🎲 " + p.name + " rolled Green\n✅ No effect",
                    "Red Dice - Safe!",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        nextTurn();
        updateStatus();
    }

    private void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        Player p = players.get(currentPlayerIndex);
        GameController.checkPrimeRule(p, gameMap);
    }

    private void checkWinCondition(Player p) {
        if (p.currentNode == gameMap.finishNode.id) {
            JOptionPane.showMessageDialog(this,
                    "🎉🏆 " + p.name + " WINS! 🏆🎉\n\nFinal Score: " + p.score + " points\nSteps Taken: " + (p.currentNode - 1),
                    "GAME OVER - VICTORY!",
                    JOptionPane.INFORMATION_MESSAGE);
            frame.showDashboard();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw Map Image
        if (mapImage != null) {
            g2d.drawImage(mapImage, 0, 0, 720, 680, null);
        } else {
            // Fallback: Draw blue background
            g2d.setColor(new Color(135, 206, 250));
            g2d.fillRect(0, 0, 720, 680);
        }

        // Draw Links
        g2d.setColor(new Color(139, 69, 19, 150));
        g2d.setStroke(new BasicStroke(3));
        for (Node n : gameMap.nodes) {
            for (Node neighbor : n.neighbors) {
                if (n.id < neighbor.id) {
                    g2d.drawLine(n.x, n.y, neighbor.x, neighbor.y);
                }
            }
        }

        // Draw Nodes
        for (Node n : gameMap.nodes) {
            n.draw(g2d);
        }

        // Draw Players dengan offset untuk multiple players
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            Node node = gameMap.getNodeById(p.currentNode);
            if (node != null) {
                // Calculate offset untuk multiple players di node yang sama
                int offsetX = 0;
                int offsetY = 0;

                if (players.size() == 2) {
                    offsetX = (i == 0) ? -15 : 15;
                } else if (players.size() == 3) {
                    offsetX = (i == 0) ? -15 : (i == 1) ? 0 : 15;
                } else if (players.size() == 4) {
                    offsetX = (i % 2 == 0) ? -15 : 15;
                    offsetY = (i < 2) ? -15 : 15;
                }

                int playerX = node.x + offsetX;
                int playerY = node.y + offsetY + 30;

                // Draw shadow
                g2d.setColor(new Color(0, 0, 0, 80));
                g2d.fillOval(playerX - 11, playerY - 9, 22, 22);

                // Draw player
                g2d.setColor(p.color);
                g2d.fillOval(playerX - 12, playerY - 12, 24, 24);

                // Draw border
                if (i == currentPlayerIndex) {
                    g2d.setColor(new Color(255, 215, 0));
                    g2d.setStroke(new BasicStroke(3));
                } else {
                    g2d.setColor(Color.WHITE);
                    g2d.setStroke(new BasicStroke(2));
                }
                g2d.drawOval(playerX - 12, playerY - 12, 24, 24);

                // Draw player number
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                String numStr = String.valueOf(i + 1);
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(numStr);
                g2d.drawString(numStr, playerX - textWidth / 2, playerY + 4);
            }
        }
    }
}