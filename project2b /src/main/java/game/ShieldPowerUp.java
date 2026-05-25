package game;

import bagel.Image;

/**
 * Shield power-up: grants the player invincibility for a fixed duration.
 * While active, an invincibility overlay image is drawn on top of the player.
 */
public class ShieldPowerUp extends PowerUp {

    private final int duration;
    private double framesRemaining;

    public ShieldPowerUp(Image image, double x, double movementSpeed,
                         double screenHeight, int duration) {
        super(image, x, movementSpeed, screenHeight);
        this.duration = duration;
        this.framesRemaining = duration;
    }

    @Override
    public void applyEffect(Player player) {
        player.activateShield(duration);
    }

}
