package logic;

// --- PERBAIKAN UTAMA: Tambahkan import ini ---
import model.Node;
// ---------------------------------------------

import java.util.*;

public class PathFinder {

    public static List<Node> dijkstra(Node start, Node target) {
        Map<Node, Integer> distance = new HashMap<>();
        Map<Node, Node> previous = new HashMap<>();

        // Priority Queue berdasarkan jarak terpendek
        // Menggunakan Comparator yang aman (getOrDefault)
        PriorityQueue<Node> pq = new PriorityQueue<>(
                Comparator.comparingInt(n -> distance.getOrDefault(n, Integer.MAX_VALUE))
        );

        // Inisialisasi awal
        distance.put(start, 0);
        pq.add(start);

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            // Jika sampai target, berhenti
            if (current.equals(target)) break;

            // Jika jarak saat ini infinity, skip
            if (distance.getOrDefault(current, Integer.MAX_VALUE) == Integer.MAX_VALUE) continue;

            for (Node neighbor : current.neighbors) {
                // Hitung bobot (Jarak Euclidean)
                int weight = (int) Math.hypot(current.x - neighbor.x, current.y - neighbor.y);

                // Bobot minimal 1
                if (weight <= 0) weight = 1;

                int newDist = distance.get(current) + weight;

                if (newDist < distance.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    distance.put(neighbor, newDist);
                    previous.put(neighbor, current);

                    // Refresh posisi di PQ
                    pq.remove(neighbor);
                    pq.add(neighbor);
                }
            }
        }

        // --- REKONSTRUKSI JALUR ---
        List<Node> path = new ArrayList<>();

        // Validasi: Jika target tidak pernah dicapai dan bukan start
        if (!previous.containsKey(target) && !start.equals(target)) {
            return Collections.emptyList();
        }

        Node step = target;
        while (step != null) {
            path.add(step);
            step = previous.get(step);
        }

        Collections.reverse(path);
        return path;
    }
}