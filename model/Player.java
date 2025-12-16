package model;

import java.awt.*;

public class Player {
    public String name;
    public int currentNode = 1;
    public int previousNode = 1; // Digunakan untuk Red Dice
    public int score = 0;
    public Color color;
    public boolean shortestPathActive = false; // Untuk Prime Number Rule

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }
}