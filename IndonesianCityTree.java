// IndonesianCityTree.java
import javax.swing.*;

public class IndonesianCityTree extends JFrame {
    public IndonesianCityTree() {
        setTitle("Navigasi Kota Indonesia - Pencari Jalur Tercepat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CityTreePanel panel = new CityTreePanel();
        add(panel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new IndonesianCityTree());
    }
}