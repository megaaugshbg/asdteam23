public class IndonesianCityTree {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Indonesian City Tree - Pathfinding");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            CityTreeVisualizer visualizer = new CityTreeVisualizer();

            // Control panel
            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new FlowLayout());

            JLabel startLabel = new JLabel("Start City:");
            JComboBox<String> startCombo = new JComboBox<>();

            JLabel endLabel = new JLabel("End City:");
            JComboBox<String> endCombo = new JComboBox<>();

            // Populate combo boxes
            for (String city : visualizer.getAllCityNames()) {
                startCombo.addItem(city);
                endCombo.addItem(city);
            }

            JButton findButton = new JButton("Find Cheapest Route");
            JLabel resultLabel = new JLabel("Select cities and click Find");

            findButton.addActionListener(e -> {
                String start = (String) startCombo.getSelectedItem();
                String end = (String) endCombo.getSelectedItem();

                if (start.equals(end)) {
                    resultLabel.setText("Start and end cities must be different!");
                    return;
                }

                visualizer.findPath(start, end);
                PathResult result = visualizer.getCurrentPath();

                if (result != null) {
                    StringBuilder pathStr = new StringBuilder("Path: ");
                    for (int i = 0; i < result.getPath().size(); i++) {
                        pathStr.append(result.getPath().get(i).getName());
                        if (i < result.getPath().size() - 1) {
                            pathStr.append(" → ");
                        }
                    }
                    resultLabel.setText(pathStr.toString() +
                            " | Total Distance: " +
                            result.getTotalDistance() + " km");
                } else {
                    resultLabel.setText("No path found!");
                }
            });

            controlPanel.add(startLabel);
            controlPanel.add(startCombo);
            controlPanel.add(endLabel);
            controlPanel.add(endCombo);
            controlPanel.add(findButton);

            JPanel infoPanel = new JPanel();
            infoPanel.add(resultLabel);

            JLabel instructions = new JLabel(
                    "<html><b>Instructions:</b> Left-click drag to move cities | " +
                            "Right-click to select | Use controls to find shortest path</html>");
            instructions.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            frame.setLayout(new BorderLayout());
            frame.add(instructions, BorderLayout.NORTH);
            frame.add(visualizer, BorderLayout.CENTER);
            frame.add(controlPanel, BorderLayout.SOUTH);
            frame.add(infoPanel, BorderLayout.PAGE_END);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}