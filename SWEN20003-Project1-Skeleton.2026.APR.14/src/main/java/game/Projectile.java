package game;

import bagel.Image;

/**
 * Projectile fired by the player, moves vertically upwards and self-destructs when off-screen.
 */
public class Projectile extends GameObject{
    private double speed;

    public Projectile(Image image, double x, double y, double speed) {
        super(image, x, y);
        this.speed = speed;
    }

    public void update(double timeScale) {
        // Projectiles travel upwards along the Y-axis
        y -= speed * timeScale;

        // Remove object once it has fully left the top of the screen
        if(y < 0 - image.getHeight()/2) {
            destroy();
        }
    }
}
