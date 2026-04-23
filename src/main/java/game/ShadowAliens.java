package game;

import bagel.AbstractGame;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import java.util.Properties;

public class ShadowAliens extends AbstractGame {
    // Global game settings shared across all classes
    private static Properties gameProps;
    public static double screenWidth;
    public static double screenHeight;

    private BattleScreen battleScreen;
    private PauseScreen pauseScreen;
    private GameState currentGameState;

    public ShadowAliens(Properties gameProps) {
        super(Integer.parseInt(gameProps.getProperty("window.width")),
                Integer.parseInt(gameProps.getProperty("window.height")),
                "Shadow Aliens");

        ShadowAliens.gameProps = gameProps;
        screenWidth = Integer.parseInt(gameProps.getProperty("window.width"));
        screenHeight = Integer.parseInt(gameProps.getProperty("window.height"));

        // Create both screens once to avoid reloading
        battleScreen = new BattleScreen(gameProps);
        pauseScreen = new PauseScreen(gameProps, battleScreen);
        currentGameState = battleScreen;

        setBackgroundColor();
    }

    private void setBackgroundColor() {
        // Read RGB values from config
        String[] backgroundColors = gameProps.getProperty("background.colour").split(",");
        double r = Double.parseDouble(backgroundColors[0]);
        double g = Double.parseDouble(backgroundColors[1]);
        double b = Double.parseDouble(backgroundColors[2]);
        Window.setClearColour(r, g, b);
    }

    @Override
    protected void update(Input input) {

        if (input.wasPressed(Keys.ESCAPE)) {
            switchGameState();
        }

        currentGameState.update(input);
        handleDevModeInput(input);
    }


    private void switchGameState() {
        boolean isBattle = currentGameState instanceof BattleScreen;
        if (isBattle) {
            currentGameState = pauseScreen;
        } else {
            currentGameState = battleScreen;
        }
    }

    // All developer shortcuts
    private void handleDevModeInput(Input input) {
        if (input.wasPressed(Keys.R)) resetGame();
        if (input.wasPressed(Keys.I)) battleScreen.toggleInvincibleMode();
        if (input.wasPressed(Keys.G)) battleScreen.increaseSpeedLevel();
        if (input.wasPressed(Keys.F)) battleScreen.decreaseSpeedLevel();
    }


    private void resetGame() {
        battleScreen = new BattleScreen(gameProps);
        pauseScreen = new PauseScreen(gameProps, battleScreen);
        currentGameState = battleScreen;
    }

    public static void main(String[] args) {
        Properties gameProps = IOUtils.readPropertiesFile(System.getProperty("gameData", "gameData.properties"));
        ShadowAliens game = new ShadowAliens(gameProps);
        game.run();
    }
}
