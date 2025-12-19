package model;

import java.util.Random;

public class Dice {
    private static final Random rand = new Random();

    // Mengembalikan angka 1 - 6
    public static int roll() {
        return rand.nextInt(6) + 1;
    }

    // Mengembalikan TRUE jika Hijau (Maju), FALSE jika Merah (Mundur)
    // 80% Hijau, 20% Merah
    public static boolean isGreen() {
        return rand.nextInt(100) < 80;
    }
}