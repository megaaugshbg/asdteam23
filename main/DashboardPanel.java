package main;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class DashboardPanel extends JPanel {

    public DashboardPanel(GameFrame frame) {
        setLayout(null);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

        JLabel title = new JLabel("Game Collection");
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setBounds(centerX - 250, centerY - 250, 500, 60);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(Color.WHITE); // Mengubah warna teks agar terlihat di atas GIF
        add(title);

        // Ukuran Tombol
        int btnWidth = 250;
        int btnHeight = 55;
        int startY = centerY - 120;
        int gap = 70;

        // Board Game Button
        JButton boardGameBtn = new JButton(" Board Game");
        boardGameBtn.setBounds(centerX - (btnWidth / 2), startY, btnWidth, btnHeight);
        boardGameBtn.setFont(new Font("Arial", Font.BOLD, 20));
        boardGameBtn.setBackground(new Color(100, 149, 237));
        boardGameBtn.setForeground(Color.WHITE);
        boardGameBtn.setFocusPainted(false);
        boardGameBtn.addActionListener(e -> {
            SoundManager.playSound("sound4.wav");
            frame.showModeSelection();
        });
        add(boardGameBtn);

        JButton mazeBtn = new JButton(" Maze Solver");
        mazeBtn.setBounds(centerX - (btnWidth / 2), startY + gap, btnWidth, btnHeight);
        mazeBtn.setFont(new Font("Arial", Font.BOLD, 20));
        mazeBtn.setBackground(new Color(76, 175, 80));
        mazeBtn.setForeground(Color.WHITE);
        mazeBtn.setFocusPainted(false);
        mazeBtn.addActionListener(e -> {
            SoundManager.playSound("sound4.wav");
            frame.showMazeGame();
        });
        add(mazeBtn);

        JButton leaderboardBtn = new JButton("Leaderboard");
        leaderboardBtn.setBounds(centerX - (btnWidth / 2), startY + (gap * 2), btnWidth, btnHeight);
        leaderboardBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        leaderboardBtn.setFocusPainted(false);
        leaderboardBtn.addActionListener(e -> {
            SoundManager.playSound("sound4.wav");
            JOptionPane.showMessageDialog(this, "Leaderboard (Coming Soon)");
        });
        add(leaderboardBtn);

        // Settings Button
        JButton settingsBtn = new JButton("Settings");
        settingsBtn.setBounds(centerX - (btnWidth / 2), startY + (gap * 3), btnWidth, btnHeight);
        settingsBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        settingsBtn.setFocusPainted(false);
        settingsBtn.addActionListener(e -> {
            SoundManager.playSound("sound4.wav");
            JOptionPane.showMessageDialog(this, "Settings (Coming Soon)");
        });
        add(settingsBtn);

        JButton exitBtn = new JButton("Exit Game");
        exitBtn.setBounds(centerX - (btnWidth / 2), startY + (gap * 4), btnWidth, btnHeight);
        exitBtn.setFont(new Font("Arial", Font.BOLD, 16));
        exitBtn.setBackground(new Color(255, 100, 100));
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setFocusPainted(false);
        exitBtn.addActionListener(e -> {
            SoundManager.playSound("sound4.wav");
            System.exit(0);
        });
        add(exitBtn);

        URL gifUrl = getClass().getResource("/Asset/main.gif");
        if (gifUrl != null) {
            ImageIcon backgroundIcon = new ImageIcon(gifUrl);
            JLabel backgroundLabel = new JLabel(backgroundIcon);

            backgroundLabel.setBounds(0, 0, screenWidth, screenHeight);
            add(backgroundLabel);
        } else {
            System.err.println("File main.gif tidak ditemukan di package Asset!");
            setBackground(Color.DARK_GRAY);
        }
    }
}