package game;

import bagel.Image;

/**
 * Abstract base class for all power-up types.
 * Power-ups move downward at a fixed speed and are destroyed when they leave the screen
 * or are collected by the player.
 */
public abstract class PowerUp extends GameObject {

    protected final double movementSpeed;
    protected final double screenHeight;

    public PowerUp(Image image, double x, double movementSpeed, double screenHeight) {
        super(image, x, -(image.getHeight() / 2.0));
        this.movementSpeed = movementSpeed;
        this.screenHeight = screenHeight;
    }


    public void update(double timeScale) {
        if (!active) return;
        y += movementSpeed * timeScale;

        double halfHeight = image.getHeight() / 2.0;
        if (y - halfHeight > screenHeight) {
            destroy();
        }
    }


    public abstract void applyEffect(Player player);

}
