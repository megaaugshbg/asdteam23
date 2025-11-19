import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

// City Node class
class City {
    private String name;
    private int x, y;
    private List<Edge> edges;
    private City parent;

    public City(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.edges = new ArrayList<>();
        this.parent = null;
    }

    public void addEdge(City destination, int distance) {
        edges.add(new Edge(destination, distance));
        destination.parent = this;
    }

    public String getName() { return name; }
    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public List<Edge> getEdges() { return edges; }
    public City getParent() { return parent; }
}

// Edge class representing connection between cities
class Edge {
    private City destination;
    private int distance;

    public Edge(City destination, int distance) {
        this.destination = destination;
        this.distance = distance;
    }

    public City getDestination() { return destination; }
    public int getDistance() { return distance; }
}

// Path result class
class PathResult {
    private List<City> path;
    private int totalDistance;

    public PathResult(List<City> path, int totalDistance) {
        this.path = path;
        this.totalDistance = totalDistance;
    }

    public List<City> getPath() { return path; }
    public int getTotalDistance() { return totalDistance; }
}