package game;

import bagel.DrawOptions;
import bagel.Font;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.util.Colour;
import java.util.Properties;

/**
 * Pause screen: renders a frozen snapshot of the battle screen, overlaid with the
 * pause title, control list, and current timescale.
 * All dev-mode controls remain active while paused — they are forwarded to the
 * BattleScreen so its state is updated correctly on unpause.
 */
public class PauseScreen extends ScreenState {

    private final BattleScreen battleScreen;

    private final Font titleFont;
    private final Font textFont;
    private final Colour textColour;

    private final String titleText;
    private final double titleY;

    private final String[] controlsList;
    private final double controlsStartY;
    private final double controlsRowGap;

    private final String timescaleText;
    private final double timescaleX;
    private final double timescaleY;

    public PauseScreen(Properties gameProps, BattleScreen battleScreen) {
        super(gameProps);
        this.battleScreen = battleScreen;

        int titleSize = Integer.parseInt(gameProps.getProperty("pausedTitle.size"));
        titleFont = new Font(gameProps.getProperty("text.font"), titleSize);

        int textSize = Integer.parseInt(gameProps.getProperty("text.size"));
        textFont = new Font(gameProps.getProperty("text.font"), textSize);

        String[] rgb = gameProps.getProperty("text.colour").split(",");
        textColour = new Colour(
                Double.parseDouble(rgb[0]),
                Double.parseDouble(rgb[1]),
                Double.parseDouble(rgb[2])
        );

        titleText = gameProps.getProperty("pausedTitle.text");
        titleY = Double.parseDouble(gameProps.getProperty("pausedTitle.posY"));

        controlsList = gameProps.getProperty("controlsList.text").split(",");
        controlsStartY = Double.parseDouble(gameProps.getProperty("controlsList.startPosY"));
        controlsRowGap = Double.parseDouble(gameProps.getProperty("controlsList.rowGap"));

        timescaleText = gameProps.getProperty("timescale.text");
        String[] tsPos = gameProps.getProperty("timescale.pos").split(",");
        timescaleX = Double.parseDouble(tsPos[0]);
        timescaleY = Double.parseDouble(tsPos[1]);
    }

    @Override
    public ScreenAction update(Input input) {
        // Dev-mode controls are still available during pause
        if (input.wasPressed(Keys.G)) battleScreen.increaseSpeed();
        if (input.wasPressed(Keys.F)) battleScreen.decreaseSpeed();
        if (input.wasPressed(Keys.I)) battleScreen.getPlayer().toggleDevInvincible();
        if (input.wasPressed(Keys.R)) return ScreenAction.RESTART_START;
        if (input.wasPressed(Keys.N)) return battleScreen.skipWave();

        if (input.wasPressed(Keys.ESCAPE)) {
            return ScreenAction.UNPAUSE;
        }

        return ScreenAction.NONE;
    }

    @Override
    public void draw() {

        battleScreen.draw();
        drawPauseOverlay();
    }

    private void drawPauseOverlay() {
        DrawOptions options = new DrawOptions().setBlendColour(textColour);


        double titleX = Window.getWidth() / 2.0 - titleFont.getWidth(titleText) / 2.0;
        titleFont.drawString(titleText, titleX, titleY, options);


        for (int i = 0; i < controlsList.length; i++) {
            String line = controlsList[i];
            double lineX = Window.getWidth() / 2.0 - textFont.getWidth(line) / 2.0;
            double lineY = controlsStartY + controlsRowGap * i;
            textFont.drawString(line, lineX, lineY, options);
        }

        String tsDisplay = timescaleText + " " + battleScreen.getTimescaleDisplay();
        textFont.drawString(tsDisplay, timescaleX, timescaleY, options);
    }
}
