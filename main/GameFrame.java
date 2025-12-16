package main;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private final CardLayout cardLayout; // Dibuat final
    private final JPanel mainPanel; // Dibuat final
    private GamePanel gamePanel; // Simpan referensi GamePanel

    public GameFrame() {
        setTitle("Map Board Game");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // REGISTER ALL PANELS
        mainPanel.add(new DashboardPanel(this), "DASHBOARD");
        mainPanel.add(new ModeSelectionPanel(this), "MODE");

        // GamePanel akan diinisialisasi saat masuk mode game

        add(mainPanel);
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

    // Metode ini sekarang menerima jumlah pemain
    public void showGame(int numPlayers) {
        // Buat instance GamePanel baru setiap kali game dimulai
        if (gamePanel != null) {
            mainPanel.remove(gamePanel);
        }
        // Pastikan GamePanel konstruktor menerima GameFrame dan numPlayers
        gamePanel = new GamePanel(this, numPlayers);
        mainPanel.add(gamePanel, "GAME");

        cardLayout.show(mainPanel, "GAME");
    }
}