package main;

import javax.swing.*;
import java.awt.*;
import maze.MazeGame;

public class GameFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private GamePanel gamePanel;
    private MazeGame mazeGame;

    public GameFrame() {
        setTitle("Map Board Game");

        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new DashboardPanel(this), "DASHBOARD");
        mainPanel.add(new ModeSelectionPanel(this), "MODE");

        add(mainPanel);

        showDashboard();
        setVisible(true);
    }

    public void showDashboard() {
        cardLayout.show(mainPanel, "DASHBOARD");
    }

    public void showModeSelection() {
        cardLayout.show(mainPanel, "MODE");
    }

    public void showGame(int numPlayers) {
        if (gamePanel != null) {
            mainPanel.remove(gamePanel);
        }
        gamePanel = new GamePanel(this, numPlayers);
        mainPanel.add(gamePanel, "GAME");
        cardLayout.show(mainPanel, "GAME");
        gamePanel.requestFocusInWindow();
    }

    public void showMazeGame() {
        if (mazeGame != null) {
            mainPanel.remove(mazeGame);
        }
        mazeGame = new MazeGame(this);
        mainPanel.add(mazeGame, "MAZE");
        cardLayout.show(mainPanel, "MAZE");
        mazeGame.requestFocusInWindow();
    }
}