package model;

import java.awt.*;

public class Player {
    public String name;
    public int currentNode = 1;
    public int previousNode = 1;
    public int score = 0;
    public Color color;
    public boolean shortestPathActive = false;

    // --- VARIABEL UNTUK ANIMASI ---
    public double visualX;
    public double visualY;
    public boolean isMoving = false;

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public void setVisualPosition(int x, int y) {
        this.visualX = x;
        this.visualY = y;
    }
}