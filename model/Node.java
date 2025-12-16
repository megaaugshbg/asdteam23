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
        this.isStar = (id % 5 == 0 && id < 64); // Star Node Rule
    }

    public void addNeighbor(Node n) {
        if (!neighbors.contains(n)) {
            neighbors.add(n);
        }
    }

    public void draw(Graphics2D g) {
        // Node background circle
        if (isStar) {
            // Star nodes - Gold color
            g.setColor(new Color(255, 215, 0));
            g.fillOval(x - 20, y - 20, 40, 40);

            g.setColor(new Color(218, 165, 32));
            g.setStroke(new BasicStroke(3));
            g.drawOval(x - 20, y - 20, 40, 40);
        } else if (id == 1) {
            // Start node - Green
            g.setColor(new Color(50, 205, 50));
            g.fillOval(x - 20, y - 20, 40, 40);

            g.setColor(new Color(34, 139, 34));
            g.setStroke(new BasicStroke(3));
            g.drawOval(x - 20, y - 20, 40, 40);
        } else if (id == 64) {
            // Finish node - Red flag
            g.setColor(new Color(220, 20, 60));
            g.fillOval(x - 20, y - 20, 40, 40);

            g.setColor(new Color(178, 34, 34));
            g.setStroke(new BasicStroke(3));
            g.drawOval(x - 20, y - 20, 40, 40);
        } else {
            // Regular nodes - White
            g.setColor(Color.WHITE);
            g.fillOval(x - 18, y - 18, 36, 36);

            g.setColor(new Color(100, 100, 100));
            g.setStroke(new BasicStroke(2));
            g.drawOval(x - 18, y - 18, 36, 36);
        }

        // Draw node number
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g.getFontMetrics();
        String idStr = String.valueOf(id);
        int textWidth = fm.stringWidth(idStr);
        int textHeight = fm.getAscent();
        g.drawString(idStr, x - textWidth / 2, y + textHeight / 4);

        // Draw star symbol for star nodes
        if (isStar) {
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.setColor(new Color(255, 255, 0));
            g.drawString("⭐", x - 8, y + 28);
        }

        // Draw START label
        if (id == 1) {
            g.setFont(new Font("Arial", Font.BOLD, 10));
            g.setColor(Color.WHITE);
            g.drawString("START", x - 18, y + 28);
        }

        // Draw FINISH flag
        if (id == 64) {
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.setColor(Color.WHITE);
            g.drawString("🏁", x - 7, y - 25);
        }

        // Draw score indicator (coin)
        if (hasScore) {
            g.setColor(new Color(255, 215, 0));
            g.fillOval(x + 15, y - 25, 12, 12);

            g.setColor(new Color(218, 165, 32));
            g.drawOval(x + 15, y - 25, 12, 12);

            g.setFont(new Font("Arial", Font.BOLD, 8));
            g.setColor(new Color(255, 69, 0));
            g.drawString("+10", x + 12, y - 30);
        }
    }

    public boolean contains(int mx, int my) {
        return Math.hypot(mx - x, my - y) <= 20;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return id == node.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Node " + id;
    }
}