package game;

import bagel.Image;

/**
 * A stationary explosion effect displayed for a fixed number of frames before being destroyed.
 * Supports large and small variants as required by the spec.
 */
public class Explosion extends GameObject {

    private final int totalDuration;
    private double framesElapsed;

    public Explosion(Image image, double x, double y, int duration) {
        super(image, x, y);
        this.totalDuration = duration;
        this.framesElapsed = 0;
    }

    /**
     * Advances the explosion timer by the current timescale.
     * Destroys itself once the duration has elapsed.
     */
    public void update(double timeScale) {
        if (!active) return;
        framesElapsed += timeScale;
        if (framesElapsed >= totalDuration) {
            destroy();
        }
    }
}
