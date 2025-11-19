import javax.swing.*;

class CityTreeVisualizer extends JPanel {
    private City root;
    private City selectedCity;
    private City draggedCity;
    private Point dragOffset;
    private City startCity;
    private City endCity;
    private PathResult currentPath;

    private static final int CITY_RADIUS = 30;
    private static final Color NODE_COLOR = new Color(52, 152, 219);
    private static final Color SELECTED_COLOR = new Color(231, 76, 60);
    private static final Color PATH_COLOR = new Color(46, 204, 113);

    public CityTreeVisualizer() {
        initializeCityTree();
        setupMouseListeners();
        setPreferredSize(new Dimension(1000, 600));
        setBackground(Color.WHITE);
    }

    private void initializeCityTree() {
        // Root city
        root = new City("Jakarta", 500, 100);

        // Level 2
        City surabaya = new City("Surabaya", 350, 200);
        City bandung = new City("Bandung", 650, 200);
        root.addEdge(surabaya, 800);
        root.addEdge(bandung, 150);

        // Level 3
        City semarang = new City("Semarang", 250, 300);
        City malang = new City("Malang", 450, 300);
        City yogyakarta = new City("Yogyakarta", 550, 300);
        City cirebon = new City("Cirebon", 750, 300);

        surabaya.addEdge(semarang, 320);
        surabaya.addEdge(malang, 90);
        bandung.addEdge(yogyakarta, 420);
        bandung.addEdge(cirebon, 200);

        // Level 4
        City solo = new City("Solo", 350, 400);
        City magelang = new City("Magelang", 650, 400);

        yogyakarta.addEdge(solo, 65);
        yogyakarta.addEdge(magelang, 45);
    }

    private void setupMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                City city = findCityAt(e.getPoint());
                if (city != null) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        draggedCity = city;
                        dragOffset = new Point(e.getX() - city.getX(),
                                e.getY() - city.getY());
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        selectedCity = city;
                        repaint();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                draggedCity = null;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedCity != null && SwingUtilities.isLeftMouseButton(e)) {
                    draggedCity.setX(e.getX() - dragOffset.x);
                    draggedCity.setY(e.getY() - dragOffset.y);
                    repaint();
                }
            }
        });
    }

    private City findCityAt(Point point) {
        return findCityAtRecursive(root, point);
    }

    private City findCityAtRecursive(City city, Point point) {
        if (city == null) return null;

        double distance = Point.distance(city.getX(), city.getY(),
                point.x, point.y);
        if (distance <= CITY_RADIUS) {
            return city;
        }

        for (Edge edge : city.getEdges()) {
            City found = findCityAtRecursive(edge.getDestination(), point);
            if (found != null) return found;
        }

        return null;
    }

    public void findPath(String startName, String endName) {
        startCity = findCityByName(root, startName);
        endCity = findCityByName(root, endName);

        if (startCity != null && endCity != null) {
            currentPath = calculatePath(startCity, endCity);
        } else {
            currentPath = null;
        }
        repaint();
    }

    private City findCityByName(City city, String name) {
        if (city == null) return null;
        if (city.getName().equals(name)) return city;

        for (Edge edge : city.getEdges()) {
            City found = findCityByName(edge.getDestination(), name);
            if (found != null) return found;
        }
        return null;
    }

    private PathResult calculatePath(City start, City end) {
        List<City> path = new ArrayList<>();
        int totalDistance = 0;

        // Find paths from root to both cities
        List<City> pathToStart = getPathFromRoot(start);
        List<City> pathToEnd = getPathFromRoot(end);

        // Find lowest common ancestor
        int commonIndex = 0;
        while (commonIndex < pathToStart.size() &&
                commonIndex < pathToEnd.size() &&
                pathToStart.get(commonIndex) == pathToEnd.get(commonIndex)) {
            commonIndex++;
        }
        commonIndex--;

        // Build path: start -> LCA -> end
        for (int i = pathToStart.size() - 1; i > commonIndex; i--) {
            path.add(pathToStart.get(i));
            totalDistance += getDistanceBetween(pathToStart.get(i),
                    pathToStart.get(i - 1));
        }

        path.add(pathToStart.get(commonIndex));

        for (int i = commonIndex + 1; i < pathToEnd.size(); i++) {
            path.add(pathToEnd.get(i));
            totalDistance += getDistanceBetween(pathToEnd.get(i - 1),
                    pathToEnd.get(i));
        }

        return new PathResult(path, totalDistance);
    }

    private List<City> getPathFromRoot(City city) {
        List<City> path = new ArrayList<>();
        City current = city;

        while (current != null) {
            path.add(0, current);
            current = current.getParent();
        }

        return path;
    }

    private int getDistanceBetween(City from, City to) {
        for (Edge edge : from.getEdges()) {
            if (edge.getDestination() == to) {
                return edge.getDistance();
            }
        }

        // Check reverse direction
        for (Edge edge : to.getEdges()) {
            if (edge.getDestination() == from) {
                return edge.getDistance();
            }
        }

        return 0;
    }

    public List<String> getAllCityNames() {
        List<String> names = new ArrayList<>();
        collectCityNames(root, names);
        return names;
    }

    private void collectCityNames(City city, List<String> names) {
        if (city == null) return;
        names.add(city.getName());
        for (Edge edge : city.getEdges()) {
            collectCityNames(edge.getDestination(), names);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw edges first
        drawEdges(g2d, root, new HashSet<>());

        // Draw path if exists
        if (currentPath != null) {
            drawPath(g2d);
        }

        // Draw cities
        drawCities(g2d, root, new HashSet<>());
    }

    private void drawEdges(Graphics2D g2d, City city, Set<City> visited) {
        if (city == null || visited.contains(city)) return;
        visited.add(city);

        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.GRAY);

        for (Edge edge : city.getEdges()) {
            City dest = edge.getDestination();
            g2d.drawLine(city.getX(), city.getY(), dest.getX(), dest.getY());

            // Draw distance label
            int midX = (city.getX() + dest.getX()) / 2;
            int midY = (city.getY() + dest.getY()) / 2;
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.drawString(edge.getDistance() + " km", midX, midY);
            g2d.setColor(Color.GRAY);

            drawEdges(g2d, dest, visited);
        }
    }

    private void drawPath(Graphics2D g2d) {
        if (currentPath.getPath().size() < 2) return;

        g2d.setStroke(new BasicStroke(4));
        g2d.setColor(PATH_COLOR);

        List<City> pathCities = currentPath.getPath();
        for (int i = 0; i < pathCities.size() - 1; i++) {
            City from = pathCities.get(i);
            City to = pathCities.get(i + 1);
            g2d.drawLine(from.getX(), from.getY(), to.getX(), to.getY());
        }
    }

    private void drawCities(Graphics2D g2d, City city, Set<City> visited) {
        if (city == null || visited.contains(city)) return;
        visited.add(city);

        // Choose color
        Color cityColor = NODE_COLOR;
        if (city == selectedCity) {
            cityColor = SELECTED_COLOR;
        } else if (currentPath != null &&
                currentPath.getPath().contains(city)) {
            cityColor = PATH_COLOR;
        }

        // Draw city circle
        g2d.setColor(cityColor);
        g2d.fillOval(city.getX() - CITY_RADIUS, city.getY() - CITY_RADIUS,
                CITY_RADIUS * 2, CITY_RADIUS * 2);

        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(city.getX() - CITY_RADIUS, city.getY() - CITY_RADIUS,
                CITY_RADIUS * 2, CITY_RADIUS * 2);

        // Draw city name
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(city.getName());
        g2d.drawString(city.getName(),
                city.getX() - textWidth / 2,
                city.getY() + 5);

        // Recursively draw children
        for (Edge edge : city.getEdges()) {
            drawCities(g2d, edge.getDestination(), visited);
        }
    }

    public PathResult getCurrentPath() {
        return currentPath;
    }
}