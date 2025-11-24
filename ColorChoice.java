// ColorChoice.java
import java.util.Random;

class ColorChoice {
    private Random random;
    public static final int GREEN = 1; // Maju
    public static final int RED = 2;   // Mundur

    public ColorChoice() {
        this.random = new Random();
    }

    public int getRandomChoice() {
        // 80% hijau, 20% merah
        int chance = random.nextInt(100);
        if (chance < 80) {
            return GREEN;
        } else {
            return RED;
        }
    }

    public String getColorName(int choice) {
        return choice == GREEN ? "HIJAU (Maju)" : "MERAH (Mundur)";
    }
}