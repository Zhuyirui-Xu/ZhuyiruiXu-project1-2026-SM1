package game;

import bagel.Font;
import bagel.Input;
import bagel.util.Colour;
import bagel.DrawOptions;
import java.util.Properties;


/**
 * Displays game controls and current timescale.
 * Draws the battle screen as a frozen background.
 */
public class PauseScreen extends ScreenState {

    private BattleScreen battleScreen;

    private Font font;
    private Font titleFont;
    private Colour colour;

    private String pauseTitle;
    private double titleY;

    private String[] controls;
    private double startY, gap;

    private String scaleText;
    private double scaleX, scaleY;

    public PauseScreen(Properties gameProps, BattleScreen battleScreen) {
        super(gameProps);
        this.battleScreen = battleScreen;

        int textSize = Integer.parseInt(gameProps.getProperty("text.size"));
        int titleSize = Integer.parseInt(gameProps.getProperty("pausedTitle.size"));

        font = new Font(gameProps.getProperty("text.font"), textSize);
        titleFont = new Font(gameProps.getProperty("text.font"), titleSize);


        String[] rgb = gameProps.getProperty("text.colour").split(",");
        colour = new Colour(Double.parseDouble(rgb[0]),
                Double.parseDouble(rgb[1]),
                Double.parseDouble(rgb[2]));

        pauseTitle = gameProps.getProperty("pausedTitle.text");
        titleY = Double.parseDouble(gameProps.getProperty("pausedTitle.posY"));


        controls = gameProps.getProperty("controlsList.text").split(",");
        startY = Double.parseDouble(gameProps.getProperty("controlsList.startPosY"));
        gap = Double.parseDouble(gameProps.getProperty("controlsList.rowGap"));

        scaleText = gameProps.getProperty("timescale.text");
        String[] pos = gameProps.getProperty("timescale.pos").split(",");
        scaleX = Double.parseDouble(pos[0]);
        scaleY = Double.parseDouble(pos[1]);
    }

    @Override
    public void update(Input input) {
        // No game logic executes while paused; only rendering is required
        draw();
    }

    @Override
    public void draw() {
        // Show the game current state behind the pause menu
        battleScreen.draw();

        // Centre title horizontally
        double titleX = ShadowAliens.screenWidth / 2 - titleFont.getWidth(pauseTitle)/2;
        titleFont.drawString(pauseTitle, titleX, titleY, new DrawOptions().setBlendColour(colour));

        // Centre each control line and space vertically using the defined gap
        for (int i = 0; i < controls.length; i++) {
            String t = controls[i].trim();
            double x = ShadowAliens.screenWidth / 2 - font.getWidth(t)/2;
            font.drawString(t, x, startY + i * gap, new DrawOptions().setBlendColour(colour));
        }

        // Display current game speed
        font.drawString(scaleText + " " + String.format("%.1f", battleScreen.computeTimeScale()),
                scaleX, scaleY, new DrawOptions().setBlendColour(colour));
    }
}
