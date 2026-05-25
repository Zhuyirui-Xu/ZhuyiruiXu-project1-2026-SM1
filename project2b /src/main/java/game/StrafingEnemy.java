package game;

import bagel.Image;

/**
 * Strafing enemy: moves down the y-axis while also moving horizontally at the same speed.
 * Starts by moving towards the nearest screen edge, bouncing back and forth until destroyed.
 */
public class StrafingEnemy extends Enemy {

    private final double screenWidth;
    private int horizontalDirection;

    public StrafingEnemy(Image image, double x, double movementSpeed,
                         double screenHeight, double screenWidth) {
        super(image, x, movementSpeed, screenHeight);
        this.screenWidth = screenWidth;


        double distanceToLeft = x;
        double distanceToRight = screenWidth - x;
        horizontalDirection = (distanceToLeft <= distanceToRight) ? -1 : 1;
    }

    @Override
    public void update(double timeScale) {
        if (!active) return;


        x += movementSpeed * horizontalDirection * timeScale;

        double halfWidth = image.getWidth() / 2.0;


        if (x - halfWidth <= 0) {
            x = halfWidth;
            horizontalDirection = 1;
        }


        if (x + halfWidth >= screenWidth) {
            x = screenWidth - halfWidth;
            horizontalDirection = -1;
        }


        super.update(timeScale);
    }
}
