package model;

import java.util.Random;

public class Dice {
    private static Random rand = new Random();

    public static int rollGreen() {
        return rand.nextInt(6) + 1;
    }

    public static boolean rollRed() {
        return rand.nextBoolean(); // true = merah
    }
}