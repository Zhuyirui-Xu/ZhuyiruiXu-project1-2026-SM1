package game;

import bagel.DrawOptions;
import bagel.Font;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.util.Colour;
import java.util.Properties;

/**
 * Start screen: shows the game title and instructions.
 * The player ship can move left and right but cannot shoot.
 * Pressing Space transitions to the Battle screen.
 */
public class StartScreen extends ScreenState {

    private final Player player;

    private final Font titleFont;
    private final Font textFont;
    private final Colour textColour;

    private final String titleText;
    private final double titleY;

    private final String[] instructionsList;
    private final double instructionsStartY;
    private final double instructionRowGap;

    public StartScreen(Properties gameProps) {
        super(gameProps);
        player = createPlayer();

        int textSize = Integer.parseInt(gameProps.getProperty("text.size"));
        textFont = new Font(gameProps.getProperty("text.font"), textSize);


        int titleSize = Integer.parseInt(gameProps.getProperty("start.title.size"));
        titleFont = new Font(gameProps.getProperty("text.font"), titleSize);

        String[] rgb = gameProps.getProperty("text.colour").split(",");
        textColour = new Colour(
                Double.parseDouble(rgb[0]),
                Double.parseDouble(rgb[1]),
                Double.parseDouble(rgb[2])
        );

        titleText = gameProps.getProperty("start.title.text");
        titleY = Double.parseDouble(gameProps.getProperty("start.title.posY"));

        instructionsList = gameProps.getProperty("start.instructionsList.text").split(",");
        instructionsStartY = Double.parseDouble(
                gameProps.getProperty("start.instructionsList.startPosY"));
        instructionRowGap = Double.parseDouble(
                gameProps.getProperty("start.instructionsList.rowGap"));
    }

    @Override
    public ScreenAction update(Input input) {
        // Player can move but not shoot on the start screen
        player.move(input, 1.0);

        if (input.wasPressed(Keys.SPACE)) {
            return ScreenAction.RESTART_BATTLE;
        }
        return ScreenAction.NONE;
    }

    @Override
    public void draw() {
        player.draw();
        drawUI();
    }

    private void drawUI() {
        DrawOptions options = new DrawOptions().setBlendColour(textColour);


        double titleX = Window.getWidth() / 2.0 - titleFont.getWidth(titleText) / 2.0;
        titleFont.drawString(titleText, titleX, titleY, options);


        for (int i = 0; i < instructionsList.length; i++) {
            String line = instructionsList[i];
            double lineX = Window.getWidth() / 2.0 - textFont.getWidth(line) / 2.0;
            double lineY = instructionsStartY + instructionRowGap * i;
            textFont.drawString(line, lineX, lineY, options);
        }
    }
}
