package maze;

import java.util.*;

public class MazeGenerator {

    private static final Random rand = new Random();

    public Cell[][] generate(int rows, int cols) {
        Cell[][] maze = new Cell[rows][cols];

<<<<<<< HEAD
        // Init cells
=======
>>>>>>> main
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                maze[r][c] = new Cell(r, c);
            }
        }

<<<<<<< HEAD
        // Prim's Algorithm
=======
>>>>>>> main
        Set<Cell> inMaze = new HashSet<>();
        List<Edge> frontier = new ArrayList<>();

        Cell start = maze[0][0];
        inMaze.add(start);
        addFrontier(start, maze, frontier);

        while (!frontier.isEmpty()) {
            Edge edge = frontier.remove(rand.nextInt(frontier.size()));

            if (!inMaze.contains(edge.to)) {
                removeWall(edge.from, edge.to);
                inMaze.add(edge.to);
                addFrontier(edge.to, maze, frontier);
            }
        }

<<<<<<< HEAD
        // Assign random terrain (except start & end)
=======
>>>>>>> main
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = maze[r][c];
                if ((r == 0 && c == 0) || (r == rows - 1 && c == cols - 1)) continue;

                int roll = rand.nextInt(100);
                if (roll < 60) cell.terrain = Terrain.GRASS;
                else if (roll < 85) cell.terrain = Terrain.MUD;
                else cell.terrain = Terrain.WATER;
            }
        }

        return maze;
    }

    private void addFrontier(Cell cell, Cell[][] maze, List<Edge> frontier) {
        int r = cell.row;
        int c = cell.col;

        if (r > 0) frontier.add(new Edge(cell, maze[r - 1][c]));
        if (r < maze.length - 1) frontier.add(new Edge(cell, maze[r + 1][c]));
        if (c > 0) frontier.add(new Edge(cell, maze[r][c - 1]));
        if (c < maze[0].length - 1) frontier.add(new Edge(cell, maze[r][c + 1]));
    }

    private void removeWall(Cell a, Cell b) {
        int dr = b.row - a.row;
        int dc = b.col - a.col;

        if (dr == -1) { a.walls[0] = false; b.walls[2] = false; } // top
        if (dr == 1)  { a.walls[2] = false; b.walls[0] = false; } // bottom
        if (dc == -1) { a.walls[3] = false; b.walls[1] = false; } // left
        if (dc == 1)  { a.walls[1] = false; b.walls[3] = false; } // right
    }

    private static class Edge {
        Cell from, to;
        Edge(Cell f, Cell t) { from = f; to = t; }
    }
}