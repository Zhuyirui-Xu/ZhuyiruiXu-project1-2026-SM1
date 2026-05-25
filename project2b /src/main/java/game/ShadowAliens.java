package game;

import bagel.AbstractGame;
import bagel.Input;
import bagel.Window;
import java.util.Properties;

/**
 * Main game class. Initialises the window and delegates all update and draw logic
 * to the ScreenManager each frame.
 */
public class ShadowAliens extends AbstractGame {

    private final Properties gameProps;
    private final ScreenManager screenManager;

    public ShadowAliens(Properties gameProps) {
        super(Integer.parseInt(gameProps.getProperty("window.width")),
                Integer.parseInt(gameProps.getProperty("window.height")),
                "Shadow Aliens");

        this.gameProps = gameProps;

        setBackgroundColour();
        screenManager = new ScreenManager(gameProps);
    }

    private void setBackgroundColour() {
        String[] parts = gameProps.getProperty("background.colour").split(",");
        double r = Double.parseDouble(parts[0]);
        double g = Double.parseDouble(parts[1]);
        double b = Double.parseDouble(parts[2]);
        Window.setClearColour(r, g, b);
    }

    @Override
    protected void update(Input input) {
        screenManager.update(input);
        screenManager.draw();
    }

    public static void main(String[] args) {
        Properties gameProps = IOUtils.readPropertiesFile(System.getProperty("gameData", "gameData.properties"));
        ShadowAliens game = new ShadowAliens(gameProps);
        game.run();
    }
}
