package game;

import bagel.Image;

/**
 * A projectile fired by a shooting enemy, moving straight down at a fixed speed.
 * Destroyed when it leaves the bottom of the screen or hits the player or a player projectile.
 */
public class EnemyProjectile extends GameObject {

    private final double speed;
    private final double screenHeight;

    public EnemyProjectile(Image image, double x, double y, double speed, double screenHeight) {
        super(image, x, y);
        this.speed = speed;
        this.screenHeight = screenHeight;
    }


    public void update(double timeScale) {
        if (!active) return;
        y += speed * timeScale;

        double halfHeight = image.getHeight() / 2.0;
        if (y - halfHeight > screenHeight) {
            destroy();
        }
    }
}
