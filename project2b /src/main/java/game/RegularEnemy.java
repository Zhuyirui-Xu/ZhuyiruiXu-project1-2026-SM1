package game;

import bagel.Image;

/**
 * Regular enemy: moves straight down the y-axis only.
 * Identical in behaviour to Project 1 enemies.
 */
public class RegularEnemy extends Enemy {

    public RegularEnemy(Image image, double x, double movementSpeed, double screenHeight) {
        super(image, x, movementSpeed, screenHeight);
    }

    @Override
    public void update(double timeScale) {
        super.update(timeScale);
    }
}
