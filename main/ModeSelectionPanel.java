package main;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;

public class ModeSelectionPanel extends JPanel {

    private final GameFrame frame;
    private final ButtonGroup playerGroup;

    public ModeSelectionPanel(GameFrame frame) {
        this.frame = frame;
        setLayout(null);
        setBackground(Color.WHITE);

        // Hitung posisi tengah layar
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = screenSize.width / 2;
        int centerY = screenSize.height / 2;

        // ===== Title =====
        JLabel title = new JLabel("Choose Number of Players");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(centerX - 300, centerY - 200, 600, 50);
        add(title);

        // ===== Radio Buttons =====
        playerGroup = new ButtonGroup();

        // Posisikan radio button agak ke kiri dari tengah
        int radioX = centerX - 60;
        int startY = centerY - 120;
        int gap = 40;

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

        // ===== Start Button =====
        JButton startBtn = new JButton("Start Game");
        startBtn.setBounds(centerX - 100, centerY + 80, 200, 50);
        startBtn.setFont(new Font("Arial", Font.BOLD, 18));
        startBtn.setBackground(new Color(100, 255, 100)); // Hijau muda
        startBtn.addActionListener(e -> {
            int numPlayers = getSelectedPlayerCount();
            frame.showGame(numPlayers);
        });
        add(startBtn);

        // ===== Back Button =====
        JButton backBtn = new JButton("Back");
        backBtn.setBounds(centerX - 100, centerY + 150, 200, 45);
        backBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        backBtn.addActionListener(e -> {
            frame.showDashboard();
        });
        add(backBtn);
    }

    private JRadioButton createRadio(String text, int x, int y, String command) {
        JRadioButton rb = new JRadioButton(text);
        rb.setBounds(x, y, 200, 30); // Lebar ditambah
        rb.setFont(new Font("Arial", Font.PLAIN, 20));
        rb.setBackground(Color.WHITE);
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