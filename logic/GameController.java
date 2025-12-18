package logic;

import model.*;

public class GameController {

    public static void movePlayer(Player p, Node next, GameMap gameMap) {
        p.previousNode = p.currentNode;
        p.currentNode = next.id;

        // --- SISTEM SKOR BARU ---
        Node currentNode = gameMap.getNodeById(p.currentNode);
        if (currentNode != null && currentNode.hasScore) {
            p.score += 100; // Tambah 100 Poin
            currentNode.hasScore = false; // Koin diambil
            System.out.println(p.name + " collected 100 points! Total: " + p.score);
        }
    }

    public static void checkPrimeRule(Player p, GameMap gameMap) {
        if (PrimeChecker.isPrime(p.currentNode)) {
            p.shortestPathActive = true;
        }
    }
}