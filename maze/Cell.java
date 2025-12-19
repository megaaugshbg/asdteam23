package maze;

import java.awt.Color;
import java.util.Objects;

public class Cell {
    public int row, col;
    public boolean[] walls = {true, true, true, true}; // Top, Right, Bottom, Left
    public Terrain terrain;

    // Visualization flags
    public boolean visited = false;
    public boolean inSolution = false;
    public boolean isStart = false;
    public boolean isEnd = false;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.terrain = Terrain.GRASS; // Default
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return row == cell.row && col == cell.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}

enum Terrain {
    GRASS(1, new Color(144, 238, 144)),     // Hijau Muda
    MUD(5, new Color(139, 90, 43)),         // Cokelat
    WATER(10, new Color(100, 149, 237));    // Biru

    public final int cost;
    public final Color color;

    Terrain(int cost, Color color) {
        this.cost = cost;
        this.color = color;
    }
}