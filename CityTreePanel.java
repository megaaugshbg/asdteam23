// CityTreePanel.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class CityTreePanel extends JPanel {
    private List<City> cities;
    private City selectedCity;
    private City draggedCity;
    private int offsetX, offsetY;
    private City pathStart, pathEnd;
    private Path currentPath;

    public CityTreePanel() {
        cities = new ArrayList<>();
        setupCities();
        setupMouseListeners();
        setPreferredSize(new Dimension(1000, 700));
        setBackground(new Color(240, 248, 255));
    }

    private void setupCities() {
        // Create cities with initial positions
        City jakarta = new City("Jakarta", 200, 100);
        City surabaya = new City("Surabaya", 400, 100);
        City bandung = new City("Bandung", 100, 250);
        City semarang = new City("Semarang", 300, 250);
        City yogyakarta = new City("Yogyakarta", 500, 250);
        City malang = new City("Malang", 600, 400);
        City solo = new City("Solo", 400, 400);
        City denpasar = new City("Denpasar", 700, 500);

        // Build graph structure with bi-directional connections and distances (in km)
        jakarta.addConnection(bandung, 150);
        jakarta.addConnection(surabaya, 800);

        bandung.addConnection(jakarta, 150);
        bandung.addConnection(semarang, 420);

        surabaya.addConnection(jakarta, 800);
        surabaya.addConnection(yogyakarta, 325);
        surabaya.addConnection(malang, 90);

        semarang.addConnection(bandung, 420);
        semarang.addConnection(solo, 110);

        yogyakarta.addConnection(surabaya, 325);
        yogyakarta.addConnection(solo, 65);

        malang.addConnection(surabaya, 90);
        malang.addConnection(denpasar, 350);

        solo.addConnection(semarang, 110);
        solo.addConnection(yogyakarta, 65);

        denpasar.addConnection(malang, 350);

        cities.add(jakarta);
        cities.add(surabaya);
        cities.add(bandung);
        cities.add(semarang);
        cities.add(yogyakarta);
        cities.add(malang);
        cities.add(solo);
        cities.add(denpasar);
    }

    private void setupMouseListeners() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (City city : cities) {
                    if (city.contains(e.getX(), e.getY())) {

                        // FIX: Mengganti isLeftButton() dengan isLeftMouseButton()
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            draggedCity = city;
                            offsetX = e.getX() - city.getX();
                            offsetY = e.getY() - city.getY();

                            // FIX: Mengganti isRightButton() dengan isRightMouseButton()
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            handleCitySelection(city);
                        }

                        repaint();
                        return;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                draggedCity = null;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedCity != null) {
                    draggedCity.setX(e.getX() - offsetX);
                    draggedCity.setY(e.getY() - offsetY);
                    repaint();
                }
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    private void handleCitySelection(City city) {
        if (pathStart == null) {
            pathStart = city;
            pathEnd = null;
            currentPath = null;
            JOptionPane.showMessageDialog(this,
                    "Start city selected: " + city.getName() +
                            "\nRight-click another city to find the path.");
        } else if (pathEnd == null && city != pathStart) {
            pathEnd = city;
            currentPath = PathFinder.findPath(pathStart, pathEnd);
            if (currentPath != null) {
                JOptionPane.showMessageDialog(this,
                        "Shortest Path:\n" + currentPath.toString());
            } else {
                JOptionPane.showMessageDialog(this,
                        "No path found between cities!");
            }
        } else {
            // Reset selection
            pathStart = city;
            pathEnd = null;
            currentPath = null;
            JOptionPane.showMessageDialog(this,
                    "Selection reset. New start city: " + city.getName());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw connections
        Set<String> drawnConnections = new HashSet<>();
        for (City city : cities) {
            for (Connection conn : city.getConnections()) {
                City dest = conn.getDestination();
                // Ensure each connection is drawn only once
                String key = city.getName().compareTo(dest.getName()) < 0 ?
                        city.getName() + "-" + dest.getName() :
                        dest.getName() + "-" + city.getName();

                if (!drawnConnections.contains(key)) {
                    boolean isInPath = isConnectionInPath(city, dest);
                    drawConnection(g2d, city, dest, conn.getDistance(), isInPath);
                    drawnConnections.add(key);
                }
            }
        }

        // Draw cities
        for (City city : cities) {
            boolean isSelected = city == pathStart || city == pathEnd;
            boolean isInPath = isInPath(city);
            drawCity(g2d, city, isSelected, isInPath);
        }

        // Draw instructions
        drawInstructions(g2d);
    }

    private void drawConnection(Graphics2D g2d, City c1, City c2,
                                int distance, boolean highlight) {
        if (highlight) {
            g2d.setColor(new Color(255, 69, 0));
            g2d.setStroke(new BasicStroke(3));
        } else {
            g2d.setColor(new Color(100, 100, 100));
            g2d.setStroke(new BasicStroke(2));
        }

        g2d.drawLine(c1.getX(), c1.getY(), c2.getX(), c2.getY());

        // Draw distance label
        int midX = (c1.getX() + c2.getX()) / 2;
        int midY = (c1.getY() + c2.getY()) / 2;
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        g2d.drawString(distance + " km", midX + 5, midY - 5);

        g2d.setStroke(new BasicStroke(1));
    }

    private void drawCity(Graphics2D g2d, City city,
                          boolean selected, boolean inPath) {
        int x = city.getX();
        int y = city.getY();
        int r = city.getRadius();

        // Draw shadow
        g2d.setColor(new Color(0, 0, 0, 30));
        g2d.fillOval(x - r + 3, y - r + 3, r * 2, r * 2);

        // Draw city circle
        if (selected) {
            g2d.setColor(new Color(255, 215, 0));
        } else if (inPath) {
            g2d.setColor(new Color(255, 140, 0));
        } else {
            g2d.setColor(new Color(70, 130, 180));
        }
        g2d.fillOval(x - r, y - r, r * 2, r * 2);

        // Draw border
        g2d.setColor(new Color(25, 25, 112));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(x - r, y - r, r * 2, r * 2);

        // Draw city name
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 11));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(city.getName());
        g2d.drawString(city.getName(), x - textWidth / 2, y + 5);
    }

    private void drawInstructions(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRoundRect(10, 10, 280, 90, 10, 10);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("Instructions:", 20, 30);
        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        g2d.drawString("• Left-click and drag to move cities", 20, 50);
        g2d.drawString("• Right-click a city to select start", 20, 65);
        g2d.drawString("• Right-click another to find path", 20, 80);
    }

    private boolean isInPath(City city) {
        if (currentPath == null) return false;
        return currentPath.getCities().contains(city);
    }

    private boolean isConnectionInPath(City c1, City c2) {
        if (currentPath == null) return false;
        List<City> pathCities = currentPath.getCities();
        for (int i = 0; i < pathCities.size() - 1; i++) {
            // Check segment (c1 -> c2) or (c2 -> c1)
            if ((pathCities.get(i) == c1 && pathCities.get(i + 1) == c2) ||
                    (pathCities.get(i) == c2 && pathCities.get(i + 1) == c1)) {
                return true;
            }
        }
        return false;
    }
}