// AnimationController.java
import javax.swing.Timer;

public class AnimationController {
    private Timer animationTimer;
    private Path currentPath;
    private int currentSegment;
    private double progress;
    private static final double ANIMATION_SPEED = 0.02;
    private CityTreePanel panel;
    private boolean isAnimating;

    public AnimationController(CityTreePanel panel) {
        this.panel = panel;
        this.currentSegment = 0;
        this.progress = 0.0;
        this.isAnimating = false;
    }

    public void startAnimation(Path path) {
        if (path == null || path.getCities().size() < 2) return;

        this.currentPath = path;
        this.currentSegment = 0;
        this.progress = 0.0;
        this.isAnimating = true;

        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }

        animationTimer = new Timer(16, e -> updateAnimation());
        animationTimer.start();
    }

    private void updateAnimation() {
        progress += ANIMATION_SPEED;

        if (progress >= 1.0) {
            progress = 0.0;
            currentSegment++;

            if (currentSegment >= currentPath.getCities().size() - 1) {
                animationTimer.stop();
                isAnimating = false;
                currentSegment = 0;
            }
        }

        panel.repaint();
    }

    public boolean isAnimating() {
        return isAnimating;
    }

    public int getCurrentX() {
        if (!isAnimating || currentPath == null) return -1;

        City from = currentPath.getCities().get(currentSegment);
        City to = currentPath.getCities().get(currentSegment + 1);

        return (int) (from.getX() + (to.getX() - from.getX()) * progress);
    }

    public int getCurrentY() {
        if (!isAnimating || currentPath == null) return -1;

        City from = currentPath.getCities().get(currentSegment);
        City to = currentPath.getCities().get(currentSegment + 1);

        return (int) (from.getY() + (to.getY() - from.getY()) * progress);
    }

    public void stopAnimation() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
        isAnimating = false;
    }
}