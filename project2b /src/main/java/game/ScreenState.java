package game;

import bagel.Image;
import bagel.Input;
import java.util.Properties;

/**
 * Abstract base class for all game screen states (Start, Battle, Pause, End).
 * Provides shared access to game properties and a factory method for creating the player.
 */
public abstract class ScreenState {

    protected final Properties gameProps;

    public ScreenState(Properties gameProps) {
        this.gameProps = gameProps;
    }


    public abstract ScreenAction update(Input input);

    public abstract void draw();


    protected Player createPlayer() {
        Image playerImage = new Image(gameProps.getProperty("player.image"));
        Image invincibilityImage = new Image(gameProps.getProperty("invincibility.image"));

        double screenWidth = Double.parseDouble(gameProps.getProperty("window.width"));
        double posY = Double.parseDouble(gameProps.getProperty("player.posY"));
        int speed = Integer.parseInt(gameProps.getProperty("player.speed"));
        int cooldown = Integer.parseInt(gameProps.getProperty("player.shootCooldown"));
        int initialLives = Integer.parseInt(gameProps.getProperty("player.initialLives"));
        int hitInvincibilityTime = Integer.parseInt(
                gameProps.getProperty("player.hitInvincibilityTime"));

        double startX = screenWidth / 2.0;
        return new Player(playerImage, invincibilityImage, startX, posY,
                speed, cooldown, initialLives, screenWidth, hitInvincibilityTime);
    }
}
