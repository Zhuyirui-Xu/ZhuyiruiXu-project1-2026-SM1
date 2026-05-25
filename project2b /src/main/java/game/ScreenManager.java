package game;

import bagel.Image;
import bagel.Input;
import java.util.Properties;

/**
 * Manages the active screen state and all transitions between screens.
 * The game loop calls update() and draw() on this each frame;
 * ScreenManager delegates to whichever screen is currently active.
 */
public class ScreenManager {

    private final Properties gameProps;

    private ScreenState currentScreen;
    private BattleScreen battleScreen;

    public ScreenManager(Properties gameProps) {
        this.gameProps = gameProps;
        switchToStart();
    }


    public void update(Input input) {
        ScreenAction action = currentScreen.update(input);
        handleAction(action);
    }


    public void draw() {
        currentScreen.draw();
    }



    private void handleAction(ScreenAction action) {
        switch (action) {
            case RESTART_BATTLE    -> switchToBattle();
            case PAUSE    -> switchToPause();
            case UNPAUSE  -> switchToUnpause();
            case WIN      -> switchToEnd(true);
            case LOSE     -> switchToEnd(false);
            case RESTART_START  -> switchToStart();
            default       -> { /* NONE — stay on current screen */ }
        }
    }

    private void switchToStart() {
        battleScreen = null;
        currentScreen = new StartScreen(gameProps);
    }

    private void switchToBattle() {
        battleScreen = new BattleScreen(gameProps);
        currentScreen = battleScreen;
    }

    private void switchToPause() {
        currentScreen = new PauseScreen(gameProps, battleScreen);
    }

    private void switchToUnpause() {
        currentScreen = battleScreen;
    }

    private void switchToEnd(boolean isWin) {
        Player endPlayer = (battleScreen != null) ? battleScreen.getPlayer() : buildDefaultPlayer();
        currentScreen = new EndScreen(gameProps, isWin, endPlayer);
        battleScreen = null;
    }

    private Player buildDefaultPlayer() {
        Image playerImage = new Image(gameProps.getProperty("player.image"));
        Image invincibilityImage = new Image(gameProps.getProperty("invincibility.image"));
        double screenWidth = Double.parseDouble(gameProps.getProperty("window.width"));
        double posY = Double.parseDouble(gameProps.getProperty("player.posY"));
        int speed = Integer.parseInt(gameProps.getProperty("player.speed"));
        int cooldown = Integer.parseInt(gameProps.getProperty("player.shootCooldown"));
        int initialLives = Integer.parseInt(gameProps.getProperty("player.initialLives"));
        int hitTime = Integer.parseInt(gameProps.getProperty("player.hitInvincibilityTime"));
        return new Player(playerImage, invincibilityImage, screenWidth / 2.0, posY,
                speed, cooldown, initialLives, screenWidth, hitTime);
    }
}
