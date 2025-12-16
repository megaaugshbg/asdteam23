package main;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    public DashboardPanel(GameFrame frame) {
        setLayout(null);
        setBackground(Color.WHITE);

        // ... Implementasi tombol-tombol (sesuai kode Anda) ...
        JLabel title = new JLabel("Map Board Game");
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setBounds(300, 100, 400, 50);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title);

        JButton playBtn = new JButton("Play");
        playBtn.setBounds(400, 200, 200, 50);
        playBtn.setFont(new Font("Arial", Font.BOLD, 18));
        playBtn.addActionListener(e -> frame.showModeSelection());
        add(playBtn);

        JButton leaderboardBtn = new JButton("Leaderboard");
        leaderboardBtn.setBounds(400, 270, 200, 45);
        add(leaderboardBtn);

        JButton settingsBtn = new JButton("Settings");
        settingsBtn.setBounds(400, 330, 200, 45);
        add(settingsBtn);

        JButton exitBtn = new JButton("Exit");
        exitBtn.setBounds(400, 390, 200, 45);
        exitBtn.addActionListener(e -> System.exit(0));
        add(exitBtn);

        leaderboardBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Leaderboard (Coming Soon)")
        );

        settingsBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Settings (Coming Soon)")
        );
    }
}