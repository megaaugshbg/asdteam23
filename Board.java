// Board.java
import java.util.ArrayList;
import java.util.List;

class Board {
    private static final int GRID_SIZE = 8;
    private static final int TOTAL_CELLS = 64;
    private List<Cell> cells;
    private int offsetX = 50;
    private int offsetY = 80;

    public Board() {
        cells = new ArrayList<>();
        initializeCells();
    }

    private void initializeCells() {
        int cellNumber = 1;

        // Ular tangga style: zigzag dari bawah ke atas
        for (int row = GRID_SIZE - 1; row >= 0; row--) {
            if ((GRID_SIZE - 1 - row) % 2 == 0) {
                // Baris genap: kiri ke kanan
                for (int col = 0; col < GRID_SIZE; col++) {
                    int x = offsetX + col * 70;
                    int y = offsetY + row * 70;
                    cells.add(new Cell(cellNumber++, x, y));
                }
            } else {
                // Baris ganjil: kanan ke kiri
                for (int col = GRID_SIZE - 1; col >= 0; col--) {
                    int x = offsetX + col * 70;
                    int y = offsetY + row * 70;
                    cells.add(new Cell(cellNumber++, x, y));
                }
            }
        }
    }

    public Cell getCell(int position) {
        if (position < 1 || position > TOTAL_CELLS) {
            return cells.get(0);
        }
        return cells.get(position - 1);
    }

    public List<Cell> getCells() { return cells; }
    public int getTotalCells() { return TOTAL_CELLS; }
    public int getGridSize() { return GRID_SIZE; }
}