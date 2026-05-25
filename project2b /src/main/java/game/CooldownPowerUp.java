package game;

import bagel.Image;

/**
 * Cooldown power-up: reduces the player's shooting cooldown by a factor of 3
 * (rounded down, minimum 1 frame) for a fixed duration.
 */
public class CooldownPowerUp extends PowerUp {

    private final int duration;
    private double framesRemaining;

    public CooldownPowerUp(Image image, double x, double movementSpeed,
                           double screenHeight, int duration) {
        super(image, x, movementSpeed, screenHeight);
        this.duration = duration;
        this.framesRemaining = duration;
    }

    @Override
    public void applyEffect(Player player) {
        player.activateCooldownBoost(duration);
    }
}
