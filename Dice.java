// Dice.java
import java.util.Random;

class Dice {
    private Random random;
    private int currentValue;

    public Dice() {
        this.random = new Random();
        this.currentValue = 1;
    }

    public int roll() {
        currentValue = random.nextInt(6) + 1;
        return currentValue;
    }

    public int getCurrentValue() { return currentValue; }
}