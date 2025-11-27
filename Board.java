// Board.java
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;

class Board {
    private static final int GRID_SIZE = 8;
    private static final int TOTAL_CELLS = 64;
    private List<Cell> cells;
    private List<Ladder> ladders;
    private int offsetX = 50;
    private int offsetY = 80;

    public Board() {
        cells = new ArrayList<>();
        ladders = new ArrayList<>();
        initializeCells();
        generateRandomLadders();
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

    private void generateRandomLadders() {
        Random random = new Random();
        Set<Integer> usedPositions = new HashSet<>();

        // Generate 5 tangga random
        for (int i = 0; i < 5; i++) {
            int start, end;
            int attempts = 0;

            do {
                // Start harus antara 2-55 agar ada ruang untuk naik
                start = random.nextInt(54) + 2;
                // End harus lebih tinggi dari start, minimal +5, maksimal +15
                int jump = random.nextInt(11) + 5; // 5 sampai 15
                end = start + jump;

                // Pastikan end tidak melebihi 63 (karena 64 adalah finish)
                if (end > 63) {
                    end = 63;
                }

                attempts++;

                // Pastikan start dan end belum digunakan
                // Dan start tidak di posisi 1 atau 64
                // Dan end tidak di posisi 1 atau 64
            } while ((usedPositions.contains(start) ||
                    usedPositions.contains(end) ||
                    start == 1 || start == 64 ||
                    end == 1 || end == 64 ||
                    start % 5 == 0 || // Hindari bonus cells
                    end % 5 == 0) &&   // Hindari bonus cells
                    attempts < 100);

            if (attempts < 100) {
                ladders.add(new Ladder(start, end));
                usedPositions.add(start);
                usedPositions.add(end);
            }
        }
    }

    public Cell getCell(int position) {
        if (position < 1 || position > TOTAL_CELLS) {
            return cells.get(0);
        }
        return cells.get(position - 1);
    }

    public Ladder getLadderAt(int position) {
        for (Ladder ladder : ladders) {
            if (ladder.getStart() == position) {
                return ladder;
            }
        }
        return null;
    }

    public List<Cell> getCells() { return cells; }
    public List<Ladder> getLadders() { return ladders; }
    public int getTotalCells() { return TOTAL_CELLS; }
    public int getGridSize() { return GRID_SIZE; }
}