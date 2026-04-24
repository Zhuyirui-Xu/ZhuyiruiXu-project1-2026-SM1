package game;

import bagel.Image;

/**
 * Visual effect that appears when an enemy is defeated and expires after a set duration.
 */
public class Explosion extends GameObject {
    private int duration;
    private double countDown;

    public Explosion(Image image, double x, double y, int duration) {
        super(image, x, y);
        this.duration = duration;
        countDown = this.duration;
    }

    public void update(double timeScale){
        // Count down based on game speed to control how long explosion is visible
        countDown -= timeScale;
        // Remove effect once the display time has elapsed
        if(countDown <= 0){
            destroy();
        }
    }
}
