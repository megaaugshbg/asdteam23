package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Node {
    public int id;
    public int x, y;
    public boolean hasScore;
    public boolean isStar;

    public List<Node> neighbors = new ArrayList<>();

    public Node(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;

<<<<<<< HEAD
        // KELIPATAN 5 ADALAH BINTANG (BONUS TURN)
        // Ini tidak mengganggu shortcut, karena shortcut butuh START di Prima
=======

>>>>>>> main
        this.isStar = (id % 5 == 0 && id < 64);
    }

    public void addNeighbor(Node n) {
        if (!neighbors.contains(n)) {
            neighbors.add(n);
        }
    }

    public void draw(Graphics2D g) {
<<<<<<< HEAD
        // --- LOGIKA GAMBAR ---
=======
>>>>>>> main
        if (isStar) {
            g.setColor(new Color(255, 215, 0)); // Emas
            g.fillOval(x - 20, y - 20, 40, 40);
            g.setColor(new Color(218, 165, 32));
            g.setStroke(new BasicStroke(3));
            g.drawOval(x - 20, y - 20, 40, 40);
        } else if (id == 1) {
            g.setColor(new Color(50, 205, 50)); // Hijau
            g.fillOval(x - 20, y - 20, 40, 40);
        } else if (id == 64) {
            g.setColor(new Color(220, 20, 60)); // Merah
            g.fillOval(x - 20, y - 20, 40, 40);
        } else {
            g.setColor(Color.WHITE);
            g.fillOval(x - 18, y - 18, 36, 36);
            g.setColor(Color.GRAY);
            g.setStroke(new BasicStroke(2));
            g.drawOval(x - 18, y - 18, 36, 36);
        }

<<<<<<< HEAD
        // Nomor
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString(String.valueOf(id), x - 6, y + 5);

        // Icon Bintang
=======

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString(String.valueOf(id), x - 6, y + 5);


>>>>>>> main
        if (isStar) {
            g.setColor(Color.YELLOW);
            g.drawString("", x - 8, y + 28);
        }

<<<<<<< HEAD
        // Icon Poin
=======

>>>>>>> main
        if (hasScore) {
            g.setColor(new Color(255, 69, 0));
            g.setFont(new Font("Arial", Font.BOLD, 10));
            g.drawString("+100", x + 10, y - 20);
        }
    }
}