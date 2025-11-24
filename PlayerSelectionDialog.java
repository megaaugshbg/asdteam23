// PlayerSelectionDialog.java
import javax.swing.*;
import java.awt.*;

class PlayerSelectionDialog extends JDialog {
    private int selectedPlayers = 0;

    public PlayerSelectionDialog(JFrame parent) {
        super(parent, "Pilih Jumlah Pemain", true);
        setLayout(new BorderLayout());
        setSize(400, 300);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Pilih Jumlah Pemain", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel);

        for (int i = 1; i <= 4; i++) {
            final int playerCount = i;
            JButton button = new JButton(playerCount + " Pemain");
            button.setFont(new Font("Arial", Font.BOLD, 16));
            button.setPreferredSize(new Dimension(200, 50));
            button.addActionListener(e -> {
                selectedPlayers = playerCount;
                dispose();
            });
            mainPanel.add(button);
        }

        add(mainPanel, BorderLayout.CENTER);
    }

    public int getSelectedPlayers() {
        return selectedPlayers;
    }
}