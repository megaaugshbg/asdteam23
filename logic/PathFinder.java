package logic;

import model.Node;

import java.util.*;

public class PathFinder {
    // ... (Implementasi dijkstra dan getAllNodes dari kode Anda)
    public static List<Node> dijkstra(Node start, Node target) {
        // ... (Implementasi Anda)
        Map<Node, Integer> distance = new HashMap<>();
        Map<Node, Node> previous = new HashMap<>();

        // Priority queue berdasarkan jarak terpendek
        PriorityQueue<Node> pq = new PriorityQueue<>(
                Comparator.comparingInt(distance::get)
        );

        // Ambil semua node, karena graph tidak harus terhubung
        Set<Node> allNodes = getAllNodes(start);

        // Init
        for (Node n : allNodes) {
            distance.put(n, Integer.MAX_VALUE);
            previous.put(n, null);
        }

        distance.put(start, 0);
        pq.add(start);

        // Dijkstra process
        while (!pq.isEmpty()) {
            Node current = pq.poll();

            if (current == target) break;

            for (Node neighbor : current.neighbors) {

                // Bobot = jarak Euclidean antar node
                // Catatan: Dalam game papan, bobot harusnya 1 per langkah,
                // tapi kita ikuti implementasi Euclidean Distance yang sudah ada.
                int weight = (int) Math.hypot(
                        current.x - neighbor.x,
                        current.y - neighbor.y
                );

                // Bobot minimal 1 jika tidak ada jarak (untuk menghindari 0)
                if (weight == 0) weight = 1;

                int newDist = distance.get(current) + weight;

                if (newDist < distance.get(neighbor)) {
                    distance.put(neighbor, newDist);
                    previous.put(neighbor, current);
                    // Update PQ secara efisien
                    if (pq.contains(neighbor)) {
                        pq.remove(neighbor);
                    }
                    pq.add(neighbor);
                }
            }
        }

        // Bangun path
        List<Node> path = new ArrayList<>();
        Node step = target;

        while (step != null) {
            path.add(step);
            step = previous.get(step);
        }

        Collections.reverse(path);
        return path.isEmpty() || !path.get(path.size() - 1).equals(target) ? Collections.emptyList() : path;
    }

    // Ambil semua node dari graph (DFS)
    private static Set<Node> getAllNodes(Node start) {
        // ... (Implementasi Anda)
        Set<Node> visited = new HashSet<>();
        Stack<Node> stack = new Stack<>();
        stack.push(start);

        // Perluas agar bisa ambil semua node di GameMap
        // Dalam implementasi ini, kita berasumsi semua node terhubung
        // (melalui jalur utama atau portal)
        // Jika tidak, perlu iterasi pada GameMap.nodes
        while (!stack.isEmpty()) {
            Node n = stack.pop();
            if (!visited.contains(n)) {
                visited.add(n);
                stack.addAll(n.neighbors);
            }
        }
        return visited;
    }
}