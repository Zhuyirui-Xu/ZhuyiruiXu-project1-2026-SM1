package game;

import bagel.AbstractGame;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import java.util.Properties;

/**
 * Main game class that manages initialising the screens and game objects
 */
public class ShadowAliens extends AbstractGame {

    private static Properties gameProps;
    public static double screenWidth;
    public static double screenHeight;

    private BattleScreen battleScreen;
    private PauseScreen pauseScreen;
    private ScreenState currentScreenState;

    public ShadowAliens(Properties gameProps) {
        super(Integer.parseInt(gameProps.getProperty("window.width")),
                Integer.parseInt(gameProps.getProperty("window.height")),
                "Shadow Aliens");

        ShadowAliens.gameProps = gameProps;
        screenWidth = Integer.parseInt(gameProps.getProperty("window.width"));
        screenHeight = Integer.parseInt(gameProps.getProperty("window.height"));

        // Initialise both states upfront to avoid repeated object creation during play
        battleScreen = new BattleScreen(gameProps);
        pauseScreen = new PauseScreen(gameProps, battleScreen);
        currentScreenState = battleScreen;

        setBackgroundColor();
    }

    private void setBackgroundColor() {
        //values are stored as comma-separated strings in properties
        String[] backgroundColors = gameProps.getProperty("background.colour").split(",");
        double r = Double.parseDouble(backgroundColors[0]);
        double g = Double.parseDouble(backgroundColors[1]);
        double b = Double.parseDouble(backgroundColors[2]);
        Window.setClearColour(r, g, b);
    }

    /**
     * Run and render the next frame.
     * @param input The current mouse/keyboard input.
     */
    @Override
    protected void update(Input input) {

        if (input.wasPressed(Keys.ESCAPE)) {
            switchGameState();
        }

        currentScreenState.update(input);
        handleDevModeInput(input);
    }


    private void switchGameState() {
        if (currentScreenState instanceof BattleScreen) {
            currentScreenState = pauseScreen;
        } else if (currentScreenState instanceof PauseScreen) {
            currentScreenState = battleScreen;
        }
    }


    private void handleDevModeInput(Input input) {
        if (input.wasPressed(Keys.R)) resetGame();
        if (input.wasPressed(Keys.I)) battleScreen.toggleInvincibleMode();
        if (input.wasPressed(Keys.G)) battleScreen.increaseSpeedLevel();
        if (input.wasPressed(Keys.F)) battleScreen.decreaseSpeedLevel();
    }


    private void resetGame() {
        battleScreen = new BattleScreen(gameProps);
        pauseScreen = new PauseScreen(gameProps, battleScreen);
        currentScreenState = battleScreen;
    }

    public static void main(String[] args) {
        Properties gameProps = IOUtils.readPropertiesFile(System.getProperty("gameData", "gameData.properties"));
        ShadowAliens game = new ShadowAliens(gameProps);
        game.run();
    }
}
