package model;

import java.util.*;

public class GameMap {

    public List<Node> nodes = new ArrayList<>();
    public Node startNode;
    public Node finishNode;
    private final int NUM_NODES = 64;

    public GameMap() {
        createNodes();
        createLinks();
        createRandomPortals();
        placeScores();
    }

    private void createNodes() {
        // KOORDINAT NODE SESUAI PETA SUPER MARIO WORLD
        // Disesuaikan dengan gambar yang Anda berikan
        // Format: {id, x, y}
        int[][] nodePositions = {
                // Pulau 1 (kiri bawah) - Start
                {1, 120, 520}, {2, 150, 490}, {3, 180, 520}, {4, 200, 480}, {5, 230, 450},

                // Pulau 2
                {6, 280, 470}, {7, 320, 450}, {8, 350, 480}, {9, 380, 460}, {10, 410, 440},

                // Bridge ke tengah
                {11, 450, 450}, {12, 490, 430}, {13, 530, 450},

                // Pulau 3 (tengah)
                {14, 570, 430}, {15, 600, 410}, {16, 630, 430}, {17, 660, 410}, {18, 690, 390},

                // Pulau 4 (kanan tengah)
                {19, 730, 410}, {20, 760, 430}, {21, 790, 450}, {22, 820, 430}, {23, 850, 410},

                // Naik ke atas
                {24, 880, 380}, {25, 900, 350}, {26, 920, 320},

                // Pulau 5 (kanan atas)
                {27, 940, 290}, {28, 960, 260}, {29, 980, 230}, {30, 1000, 200}, {31, 1020, 170},

                // Kembali ke kiri
                {32, 990, 140}, {33, 960, 120}, {34, 930, 100},

                // Pulau 6 (atas tengah)
                {35, 890, 110}, {36, 850, 130}, {37, 810, 150}, {38, 770, 130}, {39, 730, 110},

                // Bridge turun
                {40, 690, 130}, {41, 650, 150}, {42, 610, 170},

                // Pulau 7 (tengah atas)
                {43, 570, 150}, {44, 530, 170}, {45, 490, 190}, {46, 450, 170}, {47, 410, 150},

                // Turun ke kiri
                {48, 370, 170}, {49, 330, 190}, {50, 290, 210},

                // Pulau 8 (kiri tengah)
                {51, 250, 230}, {52, 210, 250}, {53, 170, 270}, {54, 130, 250}, {55, 90, 230},

                // Bridge ke bawah
                {56, 110, 290}, {57, 130, 330}, {58, 150, 370},

                // Pulau terakhir menuju finish
                {59, 180, 400}, {60, 220, 420}, {61, 260, 440}, {62, 300, 460},
                {63, 340, 480}, {64, 380, 500} // FINISH
        };

        for (int i = 0; i < NUM_NODES; i++) {
            int id = nodePositions[i][0];
            int x = nodePositions[i][1];
            int y = nodePositions[i][2];
            nodes.add(new Node(id, x, y));
        }

        startNode = nodes.get(0);
        finishNode = nodes.get(NUM_NODES - 1);
    }

    private void createLinks() {
        // Jalur utama sequential
        for (int i = 1; i < NUM_NODES; i++) {
            link(i, i + 1);
        }

        // Tambahkan beberapa jalur alternatif sesuai peta
        // Shortcuts
        link(5, 12);   // Skip beberapa node
        link(10, 17);  // Portal
        link(20, 28);  // Shortcut besar
        link(35, 42);  // Bridge alternatif
        link(48, 55);  // Shortcut
        link(15, 23);  // Jalur alternatif
    }

    private void createRandomPortals() {
        Random r = new Random();
        // Buat 5 portal acak tambahan
        for (int i = 0; i < 5; i++) {
            int a = r.nextInt(NUM_NODES - 10) + 1;
            int b = a + r.nextInt(15) + 5;
            if (b <= NUM_NODES && a != b) {
                link(a, b);
            }
        }
    }

    private void placeScores() {
        Random r = new Random();
        Set<Integer> usedNodes = new HashSet<>();

        // Tempatkan 15 score secara acak
        for (int i = 0; i < 15; i++) {
            int nodeIdx;
            do {
                nodeIdx = r.nextInt(NUM_NODES - 2) + 1; // Skip start dan finish
            } while (usedNodes.contains(nodeIdx));

            usedNodes.add(nodeIdx);
            nodes.get(nodeIdx).hasScore = true;
        }
    }

    private void link(int a, int b) {
        if (a < 1 || a > NUM_NODES || b < 1 || b > NUM_NODES) return;
        Node na = nodes.get(a - 1);
        Node nb = nodes.get(b - 1);
        na.addNeighbor(nb);
        nb.addNeighbor(na);
    }

    public Node getNodeById(int id) {
        if (id >= 1 && id <= NUM_NODES) {
            return nodes.get(id - 1);
        }
        return null;
    }
}