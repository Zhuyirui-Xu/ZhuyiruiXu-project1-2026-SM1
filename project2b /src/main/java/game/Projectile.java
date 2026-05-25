package game;

import bagel.Image;

/**
 * A projectile fired by the player, moving straight up at a fixed speed.
 * Destroyed when it leaves the top of the screen or hits an enemy.
 */
public class Projectile extends GameObject {

    private final double speed;

    public Projectile(Image image, double x, double y, double speed) {
        super(image, x, y);
        this.speed = speed;
    }


    public void update(double timeScale) {
        if (!active) return;
        y -= speed * timeScale;

        double halfHeight = image.getHeight() / 2.0;
        if (y + halfHeight < 0) {
            destroy();
        }
    }
}
