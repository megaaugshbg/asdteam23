package main;

import logic.GameController;
import logic.PathFinder;
import logic.PrimeChecker;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel {

    private final GameFrame frame;
    private final GameMap gameMap;
    private final List<Player> players;
    private int currentPlayerIndex = 0;
    private final Image mapImage;

    // UI
    private JButton rollDiceBtn, backBtn;
    private JLabel statusLabel;
    private JTextArea scoreArea;
    private JPanel statusPanel;
    private JLabel legend;

    // Animasi
    private Timer movementTimer;
    private List<Node> currentPathQueue;
    private int pathIndex = 0;
    private static final int ANIMATION_SPEED = 12;
    private static final double MOVE_SPEED = 5.0;

    public GamePanel(GameFrame frame, int numPlayers) {
        this.frame = frame;
        this.gameMap = new GameMap();
        setLayout(null);
        setBackground(Color.WHITE);

        java.net.URL imgUrl = getClass().getResource("/Asset/map.png");
        mapImage = (imgUrl != null) ? new ImageIcon(imgUrl).getImage() : null;

        players = initializePlayers(numPlayers);

        Node start = gameMap.getNodeById(1);
        for(Player p : players) p.setVisualPosition(start.x, start.y);

        setupUI();
        updateStatus();
    }

    private List<Player> initializePlayers(int count) {
        List<Player> list = new ArrayList<>();
        Color[] cols = { new Color(30, 144, 255), new Color(220, 20, 60), new Color(50, 205, 50), new Color(255, 140, 0) };
        for (int i = 0; i < count; i++) {
            list.add(new Player("Player " + (i + 1), cols[i % cols.length]));
        }
        return list;
    }

    private void setupUI() {
        Dimension sc = Toolkit.getDefaultToolkit().getScreenSize();
        int uiWidth = 260; int uiX = sc.width - uiWidth - 20;

        backBtn = new JButton("Quit Game"); backBtn.setBounds(30, 30, 150, 45);
        backBtn.setFont(new Font("Arial", Font.BOLD, 14)); backBtn.setBackground(new Color(244, 67, 54));
        backBtn.setForeground(Color.WHITE); backBtn.addActionListener(e -> frame.showDashboard()); add(backBtn);

        statusPanel = new JPanel(); statusPanel.setBounds(uiX, 50, uiWidth, 60);
        statusPanel.setBackground(new Color(100, 149, 237)); statusPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        statusLabel = new JLabel("Turn: Player 1"); statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18)); statusPanel.add(statusLabel); add(statusPanel);

        JLabel scoreTitle = new JLabel("SCOREBOARD"); scoreTitle.setBounds(uiX, 120, uiWidth, 20);
        scoreTitle.setFont(new Font("Arial", Font.BOLD, 14)); scoreTitle.setHorizontalAlignment(SwingConstants.CENTER); add(scoreTitle);
        scoreArea = new JTextArea(); scoreArea.setBounds(uiX, 145, uiWidth, 140);
        scoreArea.setEditable(false); scoreArea.setFont(new Font("Monospaced", Font.BOLD, 13));
        scoreArea.setBackground(new Color(240, 248, 255)); scoreArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); add(scoreArea);

        rollDiceBtn = new JButton("ROLL DICE"); rollDiceBtn.setBounds(uiX, 300, uiWidth, 60);
        rollDiceBtn.setBackground(new Color(255, 193, 7)); rollDiceBtn.setForeground(Color.BLACK);
        rollDiceBtn.setFont(new Font("Arial", Font.BOLD, 18)); rollDiceBtn.addActionListener(e -> rollDiceAction()); add(rollDiceBtn);

        legend = new JLabel("<html><b>Game Info:</b><br>🎲 Dice Chances:<br>🟩 80% Green (Forward)<br>🟥 20% Red (Backward)<br><br>⭐ Star Node = Bonus Turn<br>💰 Coin = +100 Score<br>🏁 = Finish Line</html>");
        legend.setBounds(uiX, 380, uiWidth, 140); legend.setFont(new Font("Arial", Font.PLAIN, 14));
        legend.setVerticalAlignment(SwingConstants.TOP); legend.setBorder(BorderFactory.createTitledBorder("Legend"));
        legend.setOpaque(true); legend.setBackground(new Color(255, 255, 255, 220)); add(legend);
    }

    private void updateStatus() {
        Player p = players.get(currentPlayerIndex);
        statusLabel.setText(p.name + "'s Turn");
        rollDiceBtn.setEnabled(!p.isMoving);
        StringBuilder sb = new StringBuilder();
        for (Player pl : players) {
            sb.append(" ").append(String.format("%-8s", pl.name)).append("| ").append(pl.score).append(" pts");
//            if(pl.shortestPathActive) sb.append(" [Prime]");
            sb.append("\n");
        }
        scoreArea.setText(sb.toString()); repaint();
    }

    private void rollDiceAction() {
        Player p = players.get(currentPlayerIndex);
        if (p.isMoving) return;

        int steps = Dice.roll();
        boolean isGreen = Dice.isGreen();
        String msg; List<Node> path;

        if (isGreen) {
            msg = "🎲Rolled " + steps + " (GREEN)\n Move FORWARD!";
            path = calculateForwardPath(p, steps);
        } else {
            msg = " Rolled " + steps + " (RED)\n Move BACKWARD!";
            path = calculateBackwardPath(p, steps);
        }

        JOptionPane.showMessageDialog(this, msg, "Dice Result",
                isGreen ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
        startAnimation(p, path);
    }

    // --- LOGIKA UTAMA: PERGERAKAN & SHORTCUT ---
    private List<Node> calculateForwardPath(Player p, int steps) {
        List<Node> path = new ArrayList<>();
        Node current = gameMap.getNodeById(p.currentNode);

        // 1. CEK APAKAH START TURN INI DARI ANGKA PRIMA?
        boolean startIsPrime = PrimeChecker.isPrime(p.currentNode);

        System.out.println("==========================================");
        System.out.println("TURN START: " + p.name + " at Node " + p.currentNode);
        System.out.println("Start is Prime? " + startIsPrime);
        System.out.println("Dice Steps: " + steps);

        // --- PENTING: KITA MATIKAN DIJKSTRA AGAR LOGIKA SHORTCUT JALAN ---
        // Jika kode ini aktif, dia akan bypass logika shortcut manual kita.
        // if (p.shortestPathActive) { ... }  <-- SAYA HAPUS/COMMENT BLOK INI

        int stepsRemaining = steps;
        Node tracker = current;

        while (stepsRemaining > 0) {
            Node next = null;
            // Cari node di depan (ID + 1)
            for(Node n : tracker.neighbors) {
                if(n.id == tracker.id + 1) { next = n; break; }
            }
            if (next == null) break;

            // 2. CEK SHORTCUT DI DEPAN
            // Apakah node 'next' adalah pintu shortcut?
            boolean isShortcutEntrance = gameMap.shortcuts.containsKey(next.id);

            if (isShortcutEntrance) {
                System.out.println("  ! Shortcut detected at Node " + next.id);

                if (startIsPrime) {
                    // SYARAT TERPENUHI (Start Prima + Ada Shortcut)

                    // A. Masuk ke mulut shortcut (misal 8)
                    path.add(next);
                    stepsRemaining--; // Langkah berkurang 1

                    // B. Teleport ke Exit (misal 13)
                    int exitId = gameMap.shortcuts.get(next.id);
                    Node exitNode = gameMap.getNodeById(exitId);

                    if (exitNode != null) {
                        path.add(exitNode);
                        tracker = exitNode; // Tracker logika pindah ke 13
                        System.out.println("  >>> JUMPING 8->13! Steps left: " + stepsRemaining);
                    }
                } else {
                    // Start BUKAN Prima, jadi lewat biasa saja
                    System.out.println("  x Shortcut ignored (Not started at Prime)");
                    path.add(next);
                    tracker = next;
                    stepsRemaining--;
                }
            } else {
                // Jalan Biasa
                path.add(next);
                tracker = next;
                stepsRemaining--;
            }
        }
        return path;
    }

    private List<Node> calculateBackwardPath(Player p, int steps) {
        List<Node> path = new ArrayList<>();
        Node tracker = gameMap.getNodeById(p.currentNode);
        for (int i = 0; i < steps; i++) {
            if (tracker.id <= 1) break;
            Node prev = gameMap.getNodeById(tracker.id - 1);
            if (prev != null) { path.add(prev); tracker = prev; }
        }
        return path;
    }

    private void startAnimation(Player p, List<Node> path) {
        if (path.isEmpty()) { nextTurn(); return; }
        p.isMoving = true; currentPathQueue = path; pathIndex = 0; updateStatus();

        movementTimer = new Timer(ANIMATION_SPEED, e -> {
            if (pathIndex >= currentPathQueue.size()) {
                ((Timer)e.getSource()).stop(); finishAnimation(p); return;
            }

            Node target = currentPathQueue.get(pathIndex);

            // --- DETEKSI TELEPORT VISUAL ---
            // Jika node target jaraknya jauh (> 1) dari node saat ini,
            // berarti itu hasil dari shortcut. Langsung Teleport!
            if (Math.abs(target.id - p.currentNode) > 1) {
                p.visualX = target.x;
                p.visualY = target.y;
                p.currentNode = target.id;
                pathIndex++;
                System.out.println("Visual Teleport executed.");
            }
            else {
                // Jalan Biasa (Interpolasi)
                double dx = target.x - p.visualX; double dy = target.y - p.visualY;
                double dist = Math.sqrt(dx*dx + dy*dy);

                if (dist <= MOVE_SPEED) {
                    p.visualX = target.x; p.visualY = target.y; p.currentNode = target.id; pathIndex++;
                } else {
                    p.visualX += (dx / dist) * MOVE_SPEED; p.visualY += (dy / dist) * MOVE_SPEED;
                }
            }
            repaint();
        });
        movementTimer.start();
    }

    private void finishAnimation(Player p) {
        p.isMoving = false;
        Node finalNode = gameMap.getNodeById(p.currentNode);
        GameController.movePlayer(p, finalNode, gameMap);

        if (p.currentNode == 64) {
            JOptionPane.showMessageDialog(this, "🏆 " + p.name + " WINS!"); frame.showDashboard(); return;
        }
        if (finalNode.isStar) {
            JOptionPane.showMessageDialog(this, "⭐ STAR NODE! Bonus Turn!"); updateStatus(); return;
        }
        nextTurn(); updateStatus();
    }

    private void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        Player p = players.get(currentPlayerIndex);
        GameController.checkPrimeRule(p, gameMap); updateStatus();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(135, 206, 250)); g2d.fillRect(0, 0, getWidth(), getHeight());

        double scale = (getHeight() * 0.95) / 680.0;
        int offX = (int) ((getWidth() - (720.0 * scale)) / 2); int offY = (int) ((getHeight() - (680.0 * scale)) / 2);

        AffineTransform old = g2d.getTransform(); g2d.translate(offX, offY); g2d.scale(scale, scale);

        if (mapImage != null) g2d.drawImage(mapImage, 0, 0, 720, 680, null);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(3));

        // GAMBAR GARIS PENGHUBUNG & SHORTCUT
        for (Node n : gameMap.nodes) {
            for (Node nb : n.neighbors) {
                if (n.id < nb.id && Math.abs(n.id - nb.id) == 1) {
                    g2d.setColor(new Color(139, 69, 19, 150)); g2d.drawLine(n.x, n.y, nb.x, nb.y);
                }
            }
            if(gameMap.shortcuts.containsKey(n.id)) {
                Node target = gameMap.getNodeById(gameMap.shortcuts.get(n.id));
                g2d.setColor(new Color(255, 0, 0, 180));
                g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, new float[]{10}, 0));
                g2d.drawLine(n.x, n.y, target.x, target.y);
            }
        }

        for(Node n : gameMap.nodes) n.draw(g2d);

        for(int i=0; i<players.size(); i++) {
            Player p = players.get(i);
            int px = (int) p.visualX + ((players.size() > 1 && i%2!=0) ? 10 : -10);
            int py = (int) p.visualY - 15;
            g2d.setColor(new Color(0,0,0,80)); g2d.fillOval(px-10, py+5, 20, 10);
            g2d.setColor(p.color); g2d.fillOval(px-10, py-10, 20, 20);
            if(i == currentPlayerIndex) { g2d.setColor(Color.YELLOW); g2d.setStroke(new BasicStroke(2)); g2d.drawOval(px-10, py-10, 20, 20); }
        }
        g2d.setTransform(old);
    }
}