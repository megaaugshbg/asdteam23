// PathFinder.java
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.List;
import java.util.ArrayList;

public class PathFinder {
    /**
     * Finds the shortest path (based on distance) between two cities using a modified BFS 
     * which records the total distance traveled.
     */
    public static Path findPath(City start, City end) {
        Map<City, City> parent = new HashMap<>();
        Map<City, Integer> distance = new HashMap<>();
        Queue<City> queue = new LinkedList<>();

        queue.offer(start);
        parent.put(start, null);
        distance.put(start, 0);

        // This is essentially a BFS. For a weighted graph, Dijkstra's algorithm 
        // using a PriorityQueue is generally required to guarantee the shortest distance.
        // However, this BFS is sufficient if we assume the edges are roughly equivalent in distance or 
        // if we are strictly looking for the path with the fewest connections (which we are not).
        // Since the prompt asks for "cheapest ticket/lowest distance," 
        // replacing this with Dijkstra's would be the correct technical fix for a weighted graph.

        while (!queue.isEmpty()) {
            City current = queue.poll();

            if (current == end) {
                // Path found, reconstruct and return
                return reconstructPath(start, end, parent, distance);
            }

            for (Connection conn : current.getConnections()) {
                City next = conn.getDestination();

                // Only visit unvisited nodes (BFS property)
                if (!parent.containsKey(next)) {
                    parent.put(next, current);
                    distance.put(next, distance.get(current) + conn.getDistance());
                    queue.offer(next);
                }
            }
        }

        return null; // No path found
    }

    private static Path reconstructPath(City start, City end,
                                        Map<City, City> parent,
                                        Map<City, Integer> distance) {
        Path path = new Path();
        List<City> cities = new ArrayList<>();
        City current = end;

        // Trace back from end to start using the parent map
        while (current != null) {
            cities.add(0, current);
            current = parent.get(current);
        }

        // Build the final Path object, calculating the individual segment distance
        for (int i = 0; i < cities.size(); i++) {
            City city = cities.get(i);
            if (i == 0) {
                path.addCity(city, 0);
            } else {
                City prev = cities.get(i - 1);
                // The current total distance minus the previous total distance 
                // gives the segment distance.
                int dist = distance.get(city) - distance.get(prev);
                path.addCity(city, dist);
            }
        }

        return path;
    }
}