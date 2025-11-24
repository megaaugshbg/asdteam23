// Cell.java
class Cell {
    private int number;
    private int x, y;
    private static final int SIZE = 70;

    public Cell(int number, int x, int y) {
        this.number = number;
        this.x = x;
        this.y = y;
    }

    public int getNumber() { return number; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getSize() { return SIZE; }
    public int getCenterX() { return x + SIZE / 2; }
    public int getCenterY() { return y + SIZE / 2; }
}
