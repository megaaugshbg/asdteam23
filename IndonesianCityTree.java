// IndonesianCityTree.java
import javax.swing.*;
import java.awt.*;

public class IndonesianCityTree extends JFrame {
    public IndonesianCityTree() {
        setTitle("Indonesian City Tree Navigator (Weighted Graph)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CityTreePanel panel = new CityTreePanel();
        add(panel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        // Runs the GUI setup on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> new IndonesianCityTree());
    }
}