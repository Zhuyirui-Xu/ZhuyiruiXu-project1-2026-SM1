package game;

import bagel.Image;

/**
 * Engine power-up: doubles the player's movement speed for a fixed duration.
 */
public class EnginePowerUp extends PowerUp {

    private final int duration;
    private double framesRemaining;

    public EnginePowerUp(Image image, double x, double movementSpeed,
                         double screenHeight, int duration) {
        super(image, x, movementSpeed, screenHeight);
        this.duration = duration;
        this.framesRemaining = duration;
    }

    @Override
    public void applyEffect(Player player) {
        player.activateEngineBoost(duration);
    }

}
