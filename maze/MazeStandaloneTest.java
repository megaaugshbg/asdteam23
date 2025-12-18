package maze;

import javax.swing.*;
import java.awt.*;

/**
 * Standalone test class for Maze Game
 * Use this to test the maze game independently without the full game framework
 *
 * Usage:
 * 1. Compile: javac maze/MazeStandaloneTest.java
 * 2. Run: java maze.MazeStandaloneTest
 */
public class MazeStandaloneTest {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Maze Game - Standalone Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Set fullscreen or large window
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setSize(screenSize.width, screenSize.height);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            // Create a mock GameFrame for the back button
            MockGameFrame mockFrame = new MockGameFrame(frame);

            // Add the maze game
            MazeGame mazeGame = new MazeGame(mockFrame);
            frame.add(mazeGame);

            frame.setVisible(true);

            System.out.println("===========================================");
            System.out.println("MAZE GAME - STANDALONE TEST");
            System.out.println("===========================================");
            System.out.println("Features:");
            System.out.println("- Generate maze with different sizes");
            System.out.println("- Solve with BFS, DFS, Dijkstra, A*");
            System.out.println("- Watch step-by-step animation");
            System.out.println("- Compare algorithm performance");
            System.out.println("===========================================");
        });
    }

    /**
     * Mock GameFrame class for standalone testing
     * Provides minimal functionality needed by MazeGame
     */
    static class MockGameFrame extends main.GameFrame {
        private JFrame parentFrame;

        public MockGameFrame(JFrame parent) {
            this.parentFrame = parent;
        }

        @Override
        public void showDashboard() {
            int choice = JOptionPane.showConfirmDialog(
                    parentFrame,
                    "Exit Maze Game?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                System.out.println("Thanks for testing Maze Game!");
                System.exit(0);
            }
        }
    }
}