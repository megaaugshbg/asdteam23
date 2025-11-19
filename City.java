// City.java
import java.util.ArrayList;
import java.util.List;

public class City {
    private String name;
    private int x, y; // Position for visualization
    private List<Connection> connections;
    private static final int RADIUS = 30;

    public City(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.connections = new ArrayList<>();
    }

    public void addConnection(City destination, int distance) {
        connections.add(new Connection(destination, distance));
    }

    // Getters and Setters
    public String getName() { return name; }
    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public List<Connection> getConnections() { return connections; }
    public int getRadius() { return RADIUS; }

    // Utility for mouse interaction
    public boolean contains(int px, int py) {
        int dx = px - x;
        int dy = py - y;
        return dx * dx + dy * dy <= RADIUS * RADIUS;
    }
}