package maze;

import java.util.*;

public class MazeSolver {

    // ===== RESULT CONTAINER =====
    public static class Result {
        public List<Cell> path;
        public Set<Cell> visitedCells;

        public Result(List<Cell> path, Set<Cell> visited) {
            this.path = path;
            this.visitedCells = visited;
        }
    }

    // ===== BFS =====
    public Result solveBFS(Cell[][] maze, Cell start, Cell end) {
        Queue<Cell> q = new LinkedList<>();
        Map<Cell, Cell> parent = new HashMap<>();
        Set<Cell> visited = new LinkedHashSet<>();

        q.add(start);
        visited.add(start);

        while (!q.isEmpty()) {
            Cell cur = q.poll();
            if (cur.equals(end)) break;

            for (Cell nb : getNeighbors(cur, maze)) {
                if (!visited.contains(nb)) {
                    visited.add(nb);
                    parent.put(nb, cur);
                    q.add(nb);
                }
            }
        }

        return new Result(buildPath(parent, start, end), visited);
    }

    // ===== DFS =====
    public Result solveDFS(Cell[][] maze, Cell start, Cell end) {
        Stack<Cell> stack = new Stack<>();
        Map<Cell, Cell> parent = new HashMap<>();
        Set<Cell> visited = new LinkedHashSet<>();

        stack.push(start);
        visited.add(start);

        while (!stack.isEmpty()) {
            Cell cur = stack.pop();
            if (cur.equals(end)) break;

            for (Cell nb : getNeighbors(cur, maze)) {
                if (!visited.contains(nb)) {
                    visited.add(nb);
                    parent.put(nb, cur);
                    stack.push(nb);
                }
            }
        }

        return new Result(buildPath(parent, start, end), visited);
    }

    // ===== DIJKSTRA =====
    public Result solveDijkstra(Cell[][] maze, Cell start, Cell end) {
        Map<Cell, Integer> dist = new HashMap<>();
        Map<Cell, Cell> parent = new HashMap<>();
        Set<Cell> visited = new LinkedHashSet<>();

        PriorityQueue<Cell> pq = new PriorityQueue<>(Comparator.comparingInt(c -> dist.getOrDefault(c, Integer.MAX_VALUE)));

        dist.put(start, 0);
        pq.add(start);

        while (!pq.isEmpty()) {
            Cell cur = pq.poll();
            if (visited.contains(cur)) continue;
            visited.add(cur);

            if (cur.equals(end)) break;

            for (Cell nb : getNeighbors(cur, maze)) {
                int newDist = dist.get(cur) + nb.terrain.cost;
                if (newDist < dist.getOrDefault(nb, Integer.MAX_VALUE)) {
                    dist.put(nb, newDist);
                    parent.put(nb, cur);
                    pq.add(nb);
                }
            }
        }

        return new Result(buildPath(parent, start, end), visited);
    }

    // ===== A* =====
    public Result solveAStar(Cell[][] maze, Cell start, Cell end) {
        Map<Cell, Integer> g = new HashMap<>();
        Map<Cell, Integer> f = new HashMap<>();
        Map<Cell, Cell> parent = new HashMap<>();
        Set<Cell> visited = new LinkedHashSet<>();

        PriorityQueue<Cell> open = new PriorityQueue<>(Comparator.comparingInt(c -> f.getOrDefault(c, Integer.MAX_VALUE)));

        g.put(start, 0);
        f.put(start, heuristic(start, end));
        open.add(start);

        while (!open.isEmpty()) {
            Cell cur = open.poll();
            if (visited.contains(cur)) continue;
            visited.add(cur);

            if (cur.equals(end)) break;

            for (Cell nb : getNeighbors(cur, maze)) {
                int tentativeG = g.get(cur) + nb.terrain.cost;
                if (tentativeG < g.getOrDefault(nb, Integer.MAX_VALUE)) {
                    parent.put(nb, cur);
                    g.put(nb, tentativeG);
                    f.put(nb, tentativeG + heuristic(nb, end));
                    open.add(nb);
                }
            }
        }

        return new Result(buildPath(parent, start, end), visited);
    }

    // ===== HELPERS =====
    private List<Cell> getNeighbors(Cell c, Cell[][] maze) {
        List<Cell> list = new ArrayList<>();
        int r = c.row, col = c.col;

        if (!c.walls[0] && r > 0) list.add(maze[r - 1][col]);
        if (!c.walls[1] && col < maze[0].length - 1) list.add(maze[r][col + 1]);
        if (!c.walls[2] && r < maze.length - 1) list.add(maze[r + 1][col]);
        if (!c.walls[3] && col > 0) list.add(maze[r][col - 1]);

        return list;
    }

    private List<Cell> buildPath(Map<Cell, Cell> parent, Cell start, Cell end) {
        List<Cell> path = new ArrayList<>();
        if (!parent.containsKey(end) && !start.equals(end)) return path;

        Cell cur = end;
        while (cur != null) {
            path.add(cur);
            cur = parent.get(cur);
        }
        Collections.reverse(path);
        return path;
    }

    private int heuristic(Cell a, Cell b) {
        return Math.abs(a.row - b.row) + Math.abs(a.col - b.col);
    }
}