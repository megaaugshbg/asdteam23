package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameMap {
    public List<Node> nodes;
    public Node finishNode;
    public Map<Integer, Integer> shortcuts;

    public GameMap() {
        nodes = new ArrayList<>();
        shortcuts = new HashMap<>();

<<<<<<< HEAD
        // --- KOORDINAT NODE (JANGAN UBAH) ---
=======
>>>>>>> main
        nodes.add(new Node(1, 100, 534)); nodes.add(new Node(2, 142, 525));
        nodes.add(new Node(3, 166, 497)); nodes.add(new Node(4, 162, 455));
        nodes.add(new Node(5, 158, 414)); nodes.add(new Node(6, 155, 366));
        nodes.add(new Node(7, 117, 340)); nodes.add(new Node(8, 119, 300));
        nodes.add(new Node(9, 119, 260)); nodes.add(new Node(10, 140, 212));
        nodes.add(new Node(11, 154, 112)); nodes.add(new Node(12, 226, 229));
        nodes.add(new Node(13, 262, 265)); nodes.add(new Node(14, 265, 306));
        nodes.add(new Node(15, 222, 351)); nodes.add(new Node(16, 336, 373));
        nodes.add(new Node(17, 348, 410)); nodes.add(new Node(18, 386, 470));
        nodes.add(new Node(19, 355, 505)); nodes.add(new Node(20, 306, 514));
        nodes.add(new Node(21, 256, 524)); nodes.add(new Node(22, 255, 571));
        nodes.add(new Node(23, 289, 603)); nodes.add(new Node(24, 333, 607));
        nodes.add(new Node(25, 375, 609)); nodes.add(new Node(26, 420, 603));
        nodes.add(new Node(27, 380, 557)); nodes.add(new Node(28, 442, 566));
        nodes.add(new Node(29, 429, 523)); nodes.add(new Node(30, 491, 550));
        nodes.add(new Node(31, 488, 500)); nodes.add(new Node(32, 469, 458));
        nodes.add(new Node(33, 512, 447)); nodes.add(new Node(34, 555, 465));
        nodes.add(new Node(35, 610, 453)); nodes.add(new Node(36, 516, 401));
        nodes.add(new Node(37, 563, 378)); nodes.add(new Node(38, 578, 340));
        nodes.add(new Node(39, 531, 338)); nodes.add(new Node(40, 459, 345));
        nodes.add(new Node(41, 499, 308)); nodes.add(new Node(42, 548, 290));
        nodes.add(new Node(43, 597, 277)); nodes.add(new Node(44, 558, 253));
        nodes.add(new Node(45, 509, 253)); nodes.add(new Node(46, 566, 217));
        nodes.add(new Node(47, 567, 179)); nodes.add(new Node(48, 530, 157));
        nodes.add(new Node(49, 492, 177)); nodes.add(new Node(50, 467, 141));
        nodes.add(new Node(51, 418, 145)); nodes.add(new Node(52, 380, 165));
        nodes.add(new Node(53, 356, 203)); nodes.add(new Node(54, 314, 198));
        nodes.add(new Node(55, 267, 178)); nodes.add(new Node(56, 227, 158));
        nodes.add(new Node(57, 227, 118)); nodes.add(new Node(58, 279, 133));
        nodes.add(new Node(59, 324, 110)); nodes.add(new Node(60, 389, 107));
        nodes.add(new Node(61, 440, 104)); nodes.add(new Node(62, 495, 105));
        nodes.add(new Node(63, 538, 104)); nodes.add(new Node(64, 595, 106));

        finishNode = getNodeById(64);
        initializeNeighbors();

<<<<<<< HEAD
        // ------------------------------------------
        // SHORTCUT PASTI (HARDCODED)
        // ------------------------------------------
=======
>>>>>>> main
        shortcuts.clear();

        shortcuts.put(8, 13);
        shortcuts.put(18, 24);
        shortcuts.put(30, 36);
        shortcuts.put(46, 52);
        shortcuts.put(55, 62);

        System.out.println(">>> FIXED SHORTCUTS LOADED: " + shortcuts);

<<<<<<< HEAD
        // KOIN RANDOM
=======

>>>>>>> main
        setupSpecialNodes();
    }

    public Node getNodeById(int id) {
        return nodes.stream().filter(n -> n.id == id).findFirst().orElse(null);
    }

    private void initializeNeighbors() {
        for (int i = 0; i < nodes.size() - 1; i++) {
            nodes.get(i).addNeighbor(nodes.get(i + 1));
        }
    }

    private void setupSpecialNodes() {
        Random rand = new Random();
        int coinsPlaced = 0;

        while (coinsPlaced < 30) {
            int randId = rand.nextInt(62) + 2;
            Node n = getNodeById(randId);
            if (n != null && !n.hasScore && !n.isStar) {
                n.hasScore = true;
                coinsPlaced++;
            }
        }
    }
}