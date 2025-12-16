package main;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;

public class ModeSelectionPanel extends JPanel {

    private final GameFrame frame; // Dibuat final
    private final ButtonGroup playerGroup; // Dibuat final

    public ModeSelectionPanel(GameFrame frame) {
        this.frame = frame;
        setLayout(null);
        setBackground(Color.WHITE);

        // ===== Title =====
        JLabel title = new JLabel("Choose Number of Players");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBounds(300, 80, 450, 40);
        add(title);

        // ===== Radio Buttons =====
        playerGroup = new ButtonGroup();

        JRadioButton p1 = createRadio("1 Player", 420, 160, "1");
        JRadioButton p2 = createRadio("2 Players", 420, 200, "2");
        JRadioButton p3 = createRadio("3 Players", 420, 240, "3");
        JRadioButton p4 = createRadio("4 Players", 420, 280, "4");

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
        startBtn.setBounds(380, 360, 200, 45);
        startBtn.setFont(new Font("Arial", Font.BOLD, 16));
        startBtn.addActionListener(e -> {
            int numPlayers = getSelectedPlayerCount();
            frame.showGame(numPlayers); // Panggilan DENGAN argumen
        });
        add(startBtn);

        // ===== Back Button =====
        JButton backBtn = new JButton("Back");
        backBtn.setBounds(380, 420, 200, 45);
        backBtn.setFont(new Font("Arial", Font.BOLD, 16));
        backBtn.addActionListener(e -> {
            frame.showDashboard();
        });
        add(backBtn);
    }

    private JRadioButton createRadio(String text, int x, int y, String command) {
        JRadioButton rb = new JRadioButton(text);
        rb.setBounds(x, y, 150, 30);
        rb.setFont(new Font("Arial", Font.PLAIN, 16));
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
                    return 1; // Default
                }
            }
        }
        return 1;
    }
}