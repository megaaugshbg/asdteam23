import java.util.*;

public class PathFinder {
    /**
     * Finds shortest path using Dijkstra's algorithm for weighted graph
     */
    public static Path findPath(City start, City end) {
        Map<City, City> parent = new HashMap<>();
        Map<City, Integer> distance = new HashMap<>();
        PriorityQueue<CityDistance> pq = new PriorityQueue<>();
        Set<City> visited = new HashSet<>();

        distance.put(start, 0);
        pq.offer(new CityDistance(start, 0));
        parent.put(start, null);

        while (!pq.isEmpty()) {
            CityDistance current = pq.poll();
            City currentCity = current.city;

            if (visited.contains(currentCity)) continue;
            visited.add(currentCity);

            if (currentCity == end) {
                return reconstructPath(start, end, parent, distance);
            }

            for (Connection conn : currentCity.getConnections()) {
                City next = conn.getDestination();
                int newDist = distance.get(currentCity) + conn.getDistance();

                if (!distance.containsKey(next) || newDist < distance.get(next)) {
                    distance.put(next, newDist);
                    parent.put(next, currentCity);
                    pq.offer(new CityDistance(next, newDist));
                }
            }
        }

        return null;
    }

    private static Path reconstructPath(City start, City end,
                                        Map<City, City> parent,
                                        Map<City, Integer> distance) {
        Path path = new Path();
        List<City> cities = new ArrayList<>();
        City current = end;

        while (current != null) {
            cities.add(0, current);
            current = parent.get(current);
        }

        for (int i = 0; i < cities.size(); i++) {
            City city = cities.get(i);
            if (i == 0) {
                path.addCity(city, 0);
            } else {
                City prev = cities.get(i - 1);
                int dist = distance.get(city) - distance.get(prev);
                path.addCity(city, dist);
            }
        }

        return path;
    }

    // INNER CLASS BARU
    static class CityDistance implements Comparable<CityDistance> {
        City city;
        int distance;

        CityDistance(City city, int distance) {
            this.city = city;
            this.distance = distance;
        }

        @Override
        public int compareTo(CityDistance other) {
            return Integer.compare(this.distance, other.distance);
        }
    }
}