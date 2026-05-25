package game;

import bagel.Image;

/**
 * Life power-up: instantly adds 1 life to the player, capped at the initial lives maximum.
 * Has no duration — the effect is applied immediately on collection.
 */
public class LifePowerUp extends PowerUp {

    public LifePowerUp(Image image, double x, double movementSpeed, double screenHeight) {
        super(image, x, movementSpeed, screenHeight);
    }

    @Override
    public void applyEffect(Player player) {
        player.gainLife();
    }
}
