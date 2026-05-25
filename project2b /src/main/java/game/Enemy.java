package game;

import bagel.Image;

/**
 * Abstract base class for all enemy ship types.
 * Handles common downward movement and off-screen destruction.
 * Subclasses implement type-specific behaviour.
 */
public abstract class Enemy extends GameObject {

    protected final double movementSpeed;
    protected final double screenHeight;

    public Enemy(Image image, double x, double movementSpeed, double screenHeight) {
        super(image, x, -(image.getHeight() / 2.0));
        this.movementSpeed = movementSpeed;
        this.screenHeight = screenHeight;
    }

    /**
     * Updates the enemy's position and behaviour each frame.
     * Subclasses call super.update() to apply downward movement and off-screen check,
     * then implement their own additional behaviour.
     */
    public void update(double timeScale) {
        if (!active) return;
        y += movementSpeed * timeScale;

        double halfHeight = image.getHeight() / 2.0;
        if (y - halfHeight > screenHeight) {
            destroy();
        }
    }

}
