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

    // UI Components
    private JButton btnBFS, btnDFS, btnDijkstra, btnAStar;
    private JButton btnGenerate, btnBack;
    private JLabel lblStatus, lblCost;
    private JComboBox<String> sizeCombo;

    public MazeGame(main.GameFrame frame) {
        this.frame = frame;
        setLayout(null);
        setBackground(new Color(240, 240, 240));

        generator = new MazeGenerator();
        solver = new MazeSolver();

        setupUI();
        generateNewMaze();
    }

    private void setupUI() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int uiX = screen.width - 280;

        btnBack = new JButton("← Back");
        btnBack.setBounds(20, 20, 120, 40);
        btnBack.setFont(new Font("Arial", Font.BOLD, 14));
        btnBack.setBackground(new Color(244, 67, 54));
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(e -> frame.showDashboard());
        add(btnBack);

        JLabel title = new JLabel("MAZE SOLVER");
        title.setBounds(uiX, 30, 250, 30);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title);

        JLabel lblSize = new JLabel("Maze Size:");
        lblSize.setBounds(uiX, 80, 100, 25);
        add(lblSize);

        sizeCombo = new JComboBox<>(new String[]{"15x15", "20x20", "25x25", "30x30"});
        sizeCombo.setSelectedIndex(2);
        sizeCombo.setBounds(uiX + 100, 80, 150, 25);
        add(sizeCombo);

        btnGenerate = new JButton("🔄 Generate New Maze");
        btnGenerate.setBounds(uiX, 120, 250, 45);
        btnGenerate.setFont(new Font("Arial", Font.BOLD, 14));
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

        btnBFS = createAlgoButton("BFS (Unweighted)", uiX, 200, "BFS");
        btnDFS = createAlgoButton("DFS (Unweighted)", uiX, 260, "DFS");
        btnDijkstra = createAlgoButton("Dijkstra (Weighted)", uiX, 320, "DIJKSTRA");
        btnAStar = createAlgoButton("A* (Weighted)", uiX, 380, "ASTAR");

        add(btnBFS); add(btnDFS); add(btnDijkstra); add(btnAStar);

        lblStatus = new JLabel("Ready");
        lblStatus.setBounds(uiX, 450, 250, 30);
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatus.setFont(new Font("Arial", Font.BOLD, 14));
        lblStatus.setOpaque(true);
        lblStatus.setBackground(Color.LIGHT_GRAY);
        add(lblStatus);

        lblCost = new JLabel("Path Cost: -");
        lblCost.setBounds(uiX, 485, 250, 25);
        lblCost.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblCost);
    }

    private JButton createAlgoButton(String text, int x, int y, String cmd) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 250, 45);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setActionCommand(cmd);
        btn.addActionListener(e -> solveWithAlgorithm(cmd));
        return btn;
    }

    private void generateNewMaze() {
        maze = generator.generate(rows, cols);
        start = maze[0][0];
        end = maze[rows - 1][cols - 1];
        start.isStart = true;
        end.isEnd = true;
        solution = null;
        visited = null;
        lblStatus.setText("Maze Generated!");
        lblStatus.setBackground(new Color(76, 175, 80));
        lblCost.setText("Path Cost: -");
        repaint();
    }

    private void solveWithAlgorithm(String algorithm) {
        if (solving) return;
        solving = true;
        setButtonsEnabled(false);
        lblStatus.setText("Solving with " + algorithm + "...");
        lblStatus.setBackground(new Color(255, 193, 7));

        for (Cell[] row : maze) {
            for (Cell c : row) {
                c.visited = false;
                c.inSolution = false;
            }
        }

        MazeSolver.Result result = switch (algorithm) {
            case "BFS" -> solver.solveBFS(maze, start, end);
            case "DFS" -> solver.solveDFS(maze, start, end);
            case "DIJKSTRA" -> solver.solveDijkstra(maze, start, end);
            case "ASTAR" -> solver.solveAStar(maze, start, end);
            default -> null;
        };

        if (result != null && result.path != null) {
            visited = result.visitedCells;
            solution = result.path;
            animateSearch();
        } else {
            lblStatus.setText("No Solution!");
            lblStatus.setBackground(Color.RED);
            solving = false;
            setButtonsEnabled(true);
        }
    }

    private void animateSearch() {
        animationIndex = 0;
        List<Cell> visitList = new ArrayList<>(visited);
        animationTimer = new Timer(25, e -> {
            if (animationIndex < visitList.size()) {
                visitList.get(animationIndex).visited = true;
                animationIndex++;
                repaint();
            } else {
                ((Timer) e.getSource()).stop();
                animateSolution();
            }
        });
        animationTimer.start();
    }

    private void animateSolution() {
        animationIndex = 0;
        Timer solutionTimer = new Timer(40, e -> {
            if (animationIndex < solution.size()) {
                solution.get(animationIndex).inSolution = true;
                animationIndex++;
                repaint();
            } else {
                ((Timer) e.getSource()).stop();
                int cost = solution.stream().mapToInt(c -> c.terrain.cost).sum();
                lblStatus.setText("Solution Found!");
                lblStatus.setBackground(new Color(76, 175, 80));
                lblCost.setText("Path Cost: " + cost + " | Steps: " + solution.size());
                solving = false;
                setButtonsEnabled(true);
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
        if (maze == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // --- DINAMIS CENTER CALCULATION ---
        int totalMazeWidth = cols * cellSize;
        int totalMazeHeight = rows * cellSize;

        // Gunakan getWidth() dan getHeight() agar otomatis menyesuaikan ukuran layar
        int offsetX = (getWidth() - totalMazeWidth) / 2;
        int offsetY = (getHeight() - totalMazeHeight) / 2;
        // ----------------------------------

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = maze[r][c];
                int x = offsetX + c * cellSize;
                int y = offsetY + r * cellSize;

                if (cell.isStart) g2d.setColor(new Color(76, 175, 80));
                else if (cell.isEnd) g2d.setColor(new Color(244, 67, 54));
                else if (cell.inSolution) g2d.setColor(Color.YELLOW);
                else if (cell.visited) g2d.setColor(new Color(173, 216, 230));
                else g2d.setColor(cell.terrain.color);

                g2d.fillRect(x, y, cellSize, cellSize);

                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(2));
                if (cell.walls[0]) g2d.drawLine(x, y, x + cellSize, y);
                if (cell.walls[1]) g2d.drawLine(x + cellSize, y, x + cellSize, y + cellSize);
                if (cell.walls[2]) g2d.drawLine(x, y + cellSize, x + cellSize, y + cellSize);
                if (cell.walls[3]) g2d.drawLine(x, y, x, y + cellSize);
            }
        }
    }
}