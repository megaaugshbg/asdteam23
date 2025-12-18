package main;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    public DashboardPanel(GameFrame frame) {
        setLayout(null);
        setBackground(Color.WHITE);

        // Ambil ukuran layar pengguna untuk penempatan posisi tengah
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

        // Judul
        JLabel title = new JLabel("Map Board Game");
        title.setFont(new Font("Arial", Font.BOLD, 48));
        // Posisikan judul di tengah (dikurangi setengah lebar label estimasi)
        title.setBounds(centerX - 250, centerY - 200, 500, 60);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title);

        // Ukuran Tombol
        int btnWidth = 250;
        int btnHeight = 55;
        int startY = centerY - 50;
        int gap = 70;

        // Play Button
        JButton playBtn = new JButton("Play");
        playBtn.setBounds(centerX - (btnWidth / 2), startY, btnWidth, btnHeight);
        playBtn.setFont(new Font("Arial", Font.BOLD, 20));
        playBtn.addActionListener(e -> frame.showModeSelection());
        add(playBtn);

        // Leaderboard Button
        JButton leaderboardBtn = new JButton("Leaderboard");
        leaderboardBtn.setBounds(centerX - (btnWidth / 2), startY + gap, btnWidth, btnHeight);
        leaderboardBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        leaderboardBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Leaderboard (Coming Soon)")
        );
        add(leaderboardBtn);

        // Settings Button
        JButton settingsBtn = new JButton("Settings");
        settingsBtn.setBounds(centerX - (btnWidth / 2), startY + (gap * 2), btnWidth, btnHeight);
        settingsBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        settingsBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Settings (Coming Soon)")
        );
        add(settingsBtn);

        // Exit Button
        JButton exitBtn = new JButton("Exit Game");
        exitBtn.setBounds(centerX - (btnWidth / 2), startY + (gap * 3), btnWidth, btnHeight);
        exitBtn.setFont(new Font("Arial", Font.BOLD, 16));
        exitBtn.setBackground(new Color(255, 100, 100));
        exitBtn.setForeground(Color.WHITE);
        exitBtn.addActionListener(e -> System.exit(0));
        add(exitBtn);
    }
}