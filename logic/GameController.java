package logic;

import model.*;

public class GameController {

    public static void movePlayer(Player p, Node next, GameMap gameMap) {
        p.previousNode = p.currentNode;
        p.currentNode = next.id;

        // Check for Score System
        Node currentNode = gameMap.getNodeById(p.currentNode);
        if (currentNode != null && currentNode.hasScore) {
            p.score += 10;
            currentNode.hasScore = false;
            System.out.println(p.name + " collected score! Total: " + p.score);
        }
    }

    public static void redDiceEffect(Player p) {
        p.currentNode = p.previousNode;
    }

    public static void checkPrimeRule(Player p, GameMap gameMap) {
        if (PrimeChecker.isPrime(p.currentNode)) {
            p.shortestPathActive = true;
            System.out.println(p.name + ": 🔥 Prime Rule Active! Next move uses Shortest Path.");
        }
    }
}