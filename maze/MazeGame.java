package maze;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MazeGame extends JPanel {

    private main.GameFrame frame;
    private MazeGenerator generator;
    private MazeSolver solver;

    private Cell[][] maze;
    private int rows = 25;
    private int cols = 25;
    private int cellSize = 25;

    private Cell start, end;
    private List<Cell> solution;
    private Set<Cell> visited;

    private Timer animationTimer;
    private int animationIndex = 0;
    private boolean solving = false;

    private Image backgroundImage;

    private JButton btnBFS, btnDFS, btnDijkstra, btnAStar;
    private JButton btnGenerate, btnBack;
    private JLabel lblStatus, lblCost;
    private JComboBox<String> sizeCombo;

    public MazeGame(main.GameFrame frame) {
        this.frame = frame;
        setLayout(null);

        try {
            java.net.URL bgUrl = getClass().getResource("/Asset/maze.jpg");
            if (bgUrl != null) {
                backgroundImage = new ImageIcon(bgUrl).getImage();
            } else {
                System.out.println("Gagal menemukan file: /Asset/maze.jpg");
                setBackground(new Color(116, 82, 55)); // Fallback warna cokelat
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        generator = new MazeGenerator();
        solver = new MazeSolver();

        setupUI();
        generateNewMaze();
    }

    private void setupUI() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int screenW = screen.width;
        int screenH = screen.height;

        int bw = 140;
        int bh = 40;
        int gap = 15;
        int totalElements = 6;

        int totalWidth = (bw * totalElements) + (gap * (totalElements - 1));
        int startX = (screenW - totalWidth) / 2;
        int bottomY = screenH - 140;

        btnBack = new JButton("← Back");
        btnBack.setBounds(20, 20, 120, 40);
        btnBack.setFont(new Font("Arial", Font.BOLD, 14));
        btnBack.setBackground(new Color(244, 67, 67));
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(e -> frame.showDashboard());
        add(btnBack);

        JLabel title = new JLabel("MAZE SOLVER");
        title.setBounds((screenW - 400) / 2, 20, 400, 40);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title);

        sizeCombo = new JComboBox<>(new String[]{"15x15", "20x20", "25x25", "30x30"});
        sizeCombo.setSelectedIndex(2);
        sizeCombo.setBounds(startX, bottomY, bw, bh);
        add(sizeCombo);

        btnGenerate = new JButton("New Maze");
        btnGenerate.setBounds(startX + (bw + gap), bottomY, bw, bh);
        btnGenerate.setFont(new Font("Arial", Font.BOLD, 13));
        btnGenerate.setBackground(new Color(76, 175, 80));
        btnGenerate.setForeground(Color.WHITE);
        btnGenerate.addActionListener(e -> {
            String size = (String) sizeCombo.getSelectedItem();
            int s = Integer.parseInt(size.split("x")[0]);
            rows = cols = s;
            cellSize = Math.min(25, 600 / s);
            generateNewMaze();
        });
        add(btnGenerate);

        btnBFS = createAlgoButton("BFS", startX + (bw + gap) * 2, bottomY, "BFS");
        btnDFS = createAlgoButton("DFS", startX + (bw + gap) * 3, bottomY, "DFS");
        btnDijkstra = createAlgoButton("Dijkstra", startX + (bw + gap) * 4, bottomY, "DIJKSTRA");
        btnAStar = createAlgoButton("A* Star", startX + (bw + gap) * 5, bottomY, "ASTAR");
        add(btnBFS); add(btnDFS); add(btnDijkstra); add(btnAStar);

        lblStatus = new JLabel("Ready");
        lblStatus.setBounds((screenW - 300) / 2, bottomY + bh + 15, 300, 30);
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatus.setFont(new Font("Arial", Font.BOLD, 14));
        lblStatus.setOpaque(true);
        lblStatus.setBackground(new Color(255, 255, 255, 200));
        add(lblStatus);

        lblCost = new JLabel("Path Cost: - | Steps: -");
        lblCost.setBounds((screenW - 400) / 2, bottomY + bh + 45, 400, 25);
        lblCost.setForeground(Color.WHITE);
        lblCost.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblCost);
    }

    private JButton createAlgoButton(String text, int x, int y, String cmd) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 140, 40);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setActionCommand(cmd);
        btn.addActionListener(e -> solveWithAlgorithm(cmd));
        return btn;
    }

    private void generateNewMaze() {
        maze = generator.generate(rows, cols);
        start = maze[0][0]; end = maze[rows - 1][cols - 1];
        start.isStart = true; end.isEnd = true;
        solution = null; visited = null;
        lblStatus.setText("Maze Generated!");
        lblStatus.setBackground(new Color(76, 175, 80, 200));
        repaint();
    }

    private void solveWithAlgorithm(String algorithm) {
        if (solving) return;
        solving = true; setButtonsEnabled(false);
        lblStatus.setText("Solving with " + algorithm + "...");
        lblStatus.setBackground(new Color(255, 193, 7, 200));

        for (Cell[] row : maze) { for (Cell c : row) { c.visited = false; c.inSolution = false; }}

        MazeSolver.Result result = switch (algorithm) {
            case "BFS" -> solver.solveBFS(maze, start, end);
            case "DFS" -> solver.solveDFS(maze, start, end);
            case "DIJKSTRA" -> solver.solveDijkstra(maze, start, end);
            case "ASTAR" -> solver.solveAStar(maze, start, end);
            default -> null;
        };

        if (result != null && result.path != null && !result.path.isEmpty()) {
            visited = result.visitedCells; solution = result.path;
            animateSearch();
        } else {
            lblStatus.setText("No Solution Found!");
            lblStatus.setBackground(new Color(244, 67, 54, 200));
            solving = false; setButtonsEnabled(true);
        }
    }

    private void animateSearch() {
        animationIndex = 0;
        List<Cell> visitList = new ArrayList<>(visited);
        animationTimer = new Timer(5, e -> {
            for (int i = 0; i < 5; i++) {
                if (animationIndex < visitList.size()) {
                    visitList.get(animationIndex).visited = true;
                    animationIndex++;
                } else {
                    ((Timer) e.getSource()).stop();
                    animateSolution();
                    break;
                }
            }
            repaint();
        });
        animationTimer.start();
    }

    private void animateSolution() {
        animationIndex = 0;
        Timer solutionTimer = new Timer(20, e -> {
            if (animationIndex < solution.size()) {
                solution.get(animationIndex).inSolution = true;
                animationIndex++;
                repaint();
            } else {
                ((Timer) e.getSource()).stop();

                main.SoundManager.playSound("sound3.wav");

                int cost = solution.stream().mapToInt(c -> c.terrain.cost).sum();
                lblStatus.setText("🏆 Solution Found!");
                lblStatus.setBackground(new Color(76, 175, 80, 200));
                lblCost.setText("Path Cost: " + cost + " | Steps: " + solution.size());
                solving = false; setButtonsEnabled(true);
            }
        });
        solutionTimer.start();
    }

    private void setButtonsEnabled(boolean enabled) {
        btnBFS.setEnabled(enabled); btnDFS.setEnabled(enabled);
        btnDijkstra.setEnabled(enabled); btnAStar.setEnabled(enabled);
        btnGenerate.setEnabled(enabled);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            g2d.setColor(new Color(0, 0, 0, 80));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

        if (maze == null) return;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int totalMazeWidth = cols * cellSize;
        int totalMazeHeight = rows * cellSize;
        int offsetX = (getWidth() - totalMazeWidth) / 2;
        int offsetY = (getHeight() - totalMazeHeight - 100) / 2 + 30;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = maze[r][c];
                int x = offsetX + c * cellSize;
                int y = offsetY + r * cellSize;

                if (cell.isStart) g2d.setColor(new Color(76, 175, 80, 220));
                else if (cell.isEnd) g2d.setColor(new Color(244, 67, 54, 220));
                else if (cell.inSolution) g2d.setColor(new Color(255, 255, 0, 200));
                else if (cell.visited) g2d.setColor(new Color(173, 216, 230, 130));
                else {
                    Color tc = cell.terrain.color;
                    g2d.setColor(new Color(tc.getRed(), tc.getGreen(), tc.getBlue(), 180));
                }

                g2d.fillRect(x, y, cellSize, cellSize);

                g2d.setColor(new Color(40, 30, 20)); //
                g2d.setStroke(new BasicStroke(2));
                if (cell.walls[0]) g2d.drawLine(x, y, x + cellSize, y);
                if (cell.walls[1]) g2d.drawLine(x + cellSize, y, x + cellSize, y + cellSize);
                if (cell.walls[2]) g2d.drawLine(x, y + cellSize, x + cellSize, y + cellSize);
                if (cell.walls[3]) g2d.drawLine(x, y, x, y + cellSize);
            }
        }
    }
}