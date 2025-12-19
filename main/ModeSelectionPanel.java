package main;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;
import java.net.URL;

public class ModeSelectionPanel extends JPanel {

    private final GameFrame frame;
    private final ButtonGroup playerGroup;
<<<<<<< HEAD
=======
    private JLabel backgroundLabel;
>>>>>>> main

    public ModeSelectionPanel(GameFrame frame) {
        this.frame = frame;
        setLayout(null);
        setOpaque(false);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

<<<<<<< HEAD
        // Hitung posisi tengah layar
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = screenSize.width / 2;
        int centerY = screenSize.height / 2;

        // ===== Title =====
        JLabel title = new JLabel("Choose Number of Players");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(centerX - 300, centerY - 200, 600, 50);
=======
        JLabel title = new JLabel("Choose Number of Players");
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(centerX - 300, centerY - 200, 600, 50);
        title.setForeground(Color.WHITE);
>>>>>>> main
        add(title);

        playerGroup = new ButtonGroup();

<<<<<<< HEAD
        // Posisikan radio button agak ke kiri dari tengah
        int radioX = centerX - 60;
        int startY = centerY - 120;
        int gap = 40;
=======
        int radioX = centerX - 80;
        int startY = centerY - 120;
        int gap = 45;
>>>>>>> main

        JRadioButton p1 = createRadio("1 Player", radioX, startY, "1");
        JRadioButton p2 = createRadio("2 Players", radioX, startY + gap, "2");
        JRadioButton p3 = createRadio("3 Players", radioX, startY + (gap * 2), "3");
        JRadioButton p4 = createRadio("4 Players", radioX, startY + (gap * 3), "4");

        p1.setSelected(true);

        playerGroup.add(p1);
        playerGroup.add(p2);
        playerGroup.add(p3);
        playerGroup.add(p4);

        add(p1);
        add(p2);
        add(p3);
        add(p4);

        JButton startBtn = new JButton("Start Game");
<<<<<<< HEAD
        startBtn.setBounds(centerX - 100, centerY + 80, 200, 50);
        startBtn.setFont(new Font("Arial", Font.BOLD, 18));
        startBtn.setBackground(new Color(100, 255, 100)); // Hijau muda
=======
        startBtn.setBounds(centerX - 100, centerY + 100, 200, 50);
        startBtn.setFont(new Font("Arial", Font.BOLD, 18));
        startBtn.setBackground(new Color(100, 255, 100));
        startBtn.setFocusPainted(false);
>>>>>>> main
        startBtn.addActionListener(e -> {
            int numPlayers = getSelectedPlayerCount();
            frame.showGame(numPlayers);
        });
        add(startBtn);

        JButton backBtn = new JButton("Back");
<<<<<<< HEAD
        backBtn.setBounds(centerX - 100, centerY + 150, 200, 45);
        backBtn.setFont(new Font("Arial", Font.PLAIN, 16));
=======
        backBtn.setBounds(centerX - 100, centerY + 170, 200, 45);
        backBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        backBtn.setFocusPainted(false);
>>>>>>> main
        backBtn.addActionListener(e -> {
            frame.showDashboard();
        });
        add(backBtn);

        URL gifUrl = getClass().getResource("/Asset/main.gif");
        if (gifUrl != null) {
            backgroundLabel = new JLabel(new ImageIcon(gifUrl));
            backgroundLabel.setBounds(0, 0, screenWidth, screenHeight);
            add(backgroundLabel);
        } else {
            System.err.println("File main.gif tidak ditemukan di package Asset!");
            setBackground(Color.DARK_GRAY);
            setOpaque(true);
        }
    }

    private JRadioButton createRadio(String text, int x, int y, String command) {
        JRadioButton rb = new JRadioButton(text);
<<<<<<< HEAD
        rb.setBounds(x, y, 200, 30); // Lebar ditambah
        rb.setFont(new Font("Arial", Font.PLAIN, 20));
        rb.setBackground(Color.WHITE);
=======
        rb.setBounds(x, y, 200, 35);
        rb.setFont(new Font("Arial", Font.BOLD, 22));
        rb.setForeground(Color.WHITE); // Teks radio button jadi putih
        rb.setOpaque(false); // SANGAT PENTING: Agar kotak radio button tidak menutupi GIF
        rb.setContentAreaFilled(false);
        rb.setBorderPainted(false);
>>>>>>> main
        rb.setActionCommand(command);
        return rb;
    }

    private int getSelectedPlayerCount() {
        for (Enumeration<AbstractButton> buttons = playerGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                try {
                    return Integer.parseInt(button.getActionCommand());
                } catch (NumberFormatException e) {
                    return 1;
                }
            }
        }
        return 1;
    }
}