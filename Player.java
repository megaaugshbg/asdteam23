// Player.java
import java.awt.Color;

class Player {
    private String name;
    private int position;
    private Color color;
    private int targetPosition;
    private boolean isMoving;
    private int playerNumber;

    public Player(String name, Color color, int playerNumber) {
        this.name = name;
        this.color = color;
        this.playerNumber = playerNumber;
        this.position = 0;
        this.targetPosition = 0;
        this.isMoving = false;
    }

    public String getName() { return name; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public Color getColor() { return color; }
    public int getTargetPosition() { return targetPosition; }
    public void setTargetPosition(int target) { this.targetPosition = target; }
    public boolean isMoving() { return isMoving; }
    public void setMoving(boolean moving) { this.isMoving = moving; }
    public int getPlayerNumber() { return playerNumber; }
}