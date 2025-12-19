package model;

import java.util.Random;

public class Dice {
    private static final Random rand = new Random();

    public static int roll() {
        return rand.nextInt(6) + 1;
    }

    public static boolean isGreen() {
        return rand.nextInt(100) < 80;
    }
}