package main;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private GamePanel gamePanel;

    public GameFrame() {
        setTitle("Map Board Game");

        // --- SETTING FULLSCREEN ---
        setUndecorated(true); // Hilangkan bingkai window (close, minimize, title bar)
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Set ukuran maksimal layar
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Opsional: Icon taskbar (jika punya logo)
        // setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Asset/icon.png")));

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // REGISTER ALL PANELS
        mainPanel.add(new DashboardPanel(this), "DASHBOARD");
        mainPanel.add(new ModeSelectionPanel(this), "MODE");

        add(mainPanel);

        // Tampilkan Dashboard awal
        showDashboard();

        setVisible(true);
    }

    // ===== NAVIGATION METHODS =====
    public void showDashboard() {
        cardLayout.show(mainPanel, "DASHBOARD");
    }

    public void showModeSelection() {
        cardLayout.show(mainPanel, "MODE");
    }

    public void showGame(int numPlayers) {
        if (gamePanel != null) {
            mainPanel.remove(gamePanel);
        }
        // Buat instance GamePanel baru
        gamePanel = new GamePanel(this, numPlayers);
        mainPanel.add(gamePanel, "GAME");

        cardLayout.show(mainPanel, "GAME");

        // Fokuskan ke panel game agar responsif
        gamePanel.requestFocusInWindow();
    }
}