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
    private AnimationController animationController;
    private JButton findPathButton, animateButton, resetButton;

    public CityTreePanel() {
        cities = new ArrayList<>();
        animationController = new AnimationController(this);
        setupCities();
        setupMouseListeners();
        setupButtons();
        setPreferredSize(new Dimension(1000, 700));
        setBackground(new Color(240, 248, 255));
    }

    private void setupButtons() {
        setLayout(null);

        findPathButton = new JButton("Find Shortest Path");
        findPathButton.setBounds(300, 620, 150, 35);
        findPathButton.setEnabled(false);
        findPathButton.addActionListener(e -> findAndDisplayPath());
        add(findPathButton);

        animateButton = new JButton("▶ Animate");
        animateButton.setBounds(460, 620, 120, 35);
        animateButton.setEnabled(false);
        animateButton.addActionListener(e -> startAnimation());
        add(animateButton);

        resetButton = new JButton("Reset");
        resetButton.setBounds(590, 620, 100, 35);
        resetButton.addActionListener(e -> resetSelection());
        add(resetButton);
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
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            draggedCity = city;
                            offsetX = e.getX() - city.getX();
                            offsetY = e.getY() - city.getY();
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
            animationController.stopAnimation();
            findPathButton.setEnabled(false);
            animateButton.setEnabled(false);
            repaint();
        } else if (pathEnd == null && city != pathStart) {
            pathEnd = city;
            findPathButton.setEnabled(true);
            repaint();
        } else {
            pathStart = city;
            pathEnd = null;
            currentPath = null;
            animationController.stopAnimation();
            findPathButton.setEnabled(false);
            animateButton.setEnabled(false);
            repaint();
        }
    }

    private void findAndDisplayPath() {
        if (pathStart != null && pathEnd != null) {
            currentPath = PathFinder.findPath(pathStart, pathEnd);
            if (currentPath != null) {
                animateButton.setEnabled(true);
                JOptionPane.showMessageDialog(this,
                        "Jalur Tercepat Ditemukan!\n\n" + currentPath.toString() +
                                "\n\nKlik tombol 'Animate' untuk melihat animasi perjalanan.");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Tidak ada jalur yang ditemukan antara kedua kota!");
            }
            repaint();
        }
    }

    private void startAnimation() {
        if (currentPath != null) {
            animationController.startAnimation(currentPath);
        }
    }

    private void resetSelection() {
        pathStart = null;
        pathEnd = null;
        currentPath = null;
        animationController.stopAnimation();
        findPathButton.setEnabled(false);
        animateButton.setEnabled(false);
        repaint();
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

        // Draw animated vehicle
        if (animationController.isAnimating()) {
            drawVehicle(g2d);
        }

        // Draw instructions
        drawInstructions(g2d);

        // Draw status
        drawStatus(g2d);
    }

    private void drawVehicle(Graphics2D g2d) {
        int x = animationController.getCurrentX();
        int y = animationController.getCurrentY();

        if (x < 0 || y < 0) return;

        // Draw airplane shadow
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.fillOval(x - 12, y - 8, 24, 16);

        // Draw airplane body
        g2d.setColor(new Color(255, 69, 0));
        int[] xPoints = {x, x - 10, x, x + 10};
        int[] yPoints = {y - 8, y, y + 8, y};
        g2d.fillPolygon(xPoints, yPoints, 4);

        // Draw airplane wings
        g2d.setColor(new Color(255, 140, 0));
        g2d.fillOval(x - 6, y - 6, 12, 12);

        // Draw trail effect
        for (int i = 1; i <= 3; i++) {
            g2d.setColor(new Color(255, 140, 0, 100 - i * 30));
            g2d.fillOval(x - 15 - i * 5, y - 2, 4, 4);
        }
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
        g2d.fillRoundRect(10, 10, 280, 110, 10, 10);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("Instruksi:", 20, 30);
        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        g2d.drawString("• Klik kiri + drag untuk geser kota", 20, 50);
        g2d.drawString("• Klik kanan untuk pilih kota awal", 20, 65);
        g2d.drawString("• Klik kanan lagi untuk pilih tujuan", 20, 80);
        g2d.drawString("• Klik 'Find Shortest Path' untuk cari", 20, 95);
        g2d.drawString("• Klik 'Animate' untuk animasi", 20, 110);
    }

    private void drawStatus(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRoundRect(710, 10, 270, 90, 10, 10);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("Status:", 720, 30);
        g2d.setFont(new Font("Arial", Font.PLAIN, 11));

        String startText = pathStart != null ? pathStart.getName() : "-";
        String endText = pathEnd != null ? pathEnd.getName() : "-";
        String distText = currentPath != null ? currentPath.getTotalDistance() + " km" : "-";

        g2d.drawString("Kota Awal: " + startText, 720, 50);
        g2d.drawString("Kota Tujuan: " + endText, 720, 65);
        g2d.drawString("Total Jarak: " + distText, 720, 80);
    }

    private boolean isInPath(City city) {
        if (currentPath == null) return false;
        return currentPath.getCities().contains(city);
    }

    private boolean isConnectionInPath(City c1, City c2) {
        if (currentPath == null) return false;
        List<City> pathCities = currentPath.getCities();
        for (int i = 0; i < pathCities.size() - 1; i++) {
            if ((pathCities.get(i) == c1 && pathCities.get(i + 1) == c2) ||
                    (pathCities.get(i) == c2 && pathCities.get(i + 1) == c1)) {
                return true;
            }
        }
        return false;
    }
}