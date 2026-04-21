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

    //Add attributes
    private BattleScreen battleScreen;
    private PauseScreen pauseScreen;
    private Screen currentScreen;

    public ShadowAliens(Properties gameProps) {
        super(Integer.parseInt(gameProps.getProperty("window.width")),
                Integer.parseInt(gameProps.getProperty("window.height")),
                "Shadow Aliens");

        ShadowAliens.gameProps = gameProps;
        screenWidth = Integer.parseInt(gameProps.getProperty("window.width"));
        screenHeight = Integer.parseInt(gameProps.getProperty("window.height"));


        //Initialize Screens
        battleScreen = new BattleScreen(gameProps);
        pauseScreen = new PauseScreen(gameProps, battleScreen);
        currentScreen = battleScreen;
        //Initialize background color
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
        //Switch Screens when ESC pressed
        if (input.wasPressed(Keys.ESCAPE)) {
            if (currentScreen instanceof BattleScreen) {
                currentScreen = pauseScreen;   //can write as private helper method here
            }else if (currentScreen instanceof PauseScreen) {
                currentScreen = battleScreen;
            }
        }
        currentScreen.update(input);

        //DEV mode features
        //R:reset
        if(input.wasPressed(Keys.R)){
            resetGame();
        }
        //I:invincible
        if(input.wasPressed(Keys.I)){
            battleScreen.switchToInvincible();
        }
        //G:speed up
        if(input.wasPressed(Keys.G)){
            battleScreen.speedUp();
        }

        //F:speed down
        if(input.wasPressed(Keys.F)) {
            battleScreen.speedDown();
        }
    }

    public static void main(String[] args) {
        Properties gameProps = IOUtils.readPropertiesFile(System.getProperty("gameData","gameData.properties"));
        ShadowAliens game = new ShadowAliens(gameProps);
        game.run();
    }

    private void resetGame(){
        battleScreen = new BattleScreen(gameProps);
        pauseScreen = new PauseScreen(gameProps, battleScreen);
        currentScreen = battleScreen;
    }
}
