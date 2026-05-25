package game;

import bagel.DrawOptions;
import bagel.Font;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.util.Colour;
import java.util.Properties;

/**
 * End screen: shown after winning or losing. The player ship is visible and can move
 * but cannot shoot. Pressing Space restarts the game from the Battle screen.
 */
public class EndScreen extends ScreenState {

    private final Player player;
    private final boolean isWin;

    private final Font winFont;
    private final Font loseFont;
    private final Font textFont;
    private final Colour textColour;

    private final String winText;
    private final double winY;

    private final String loseText;
    private final double loseY;

    private final String[] instructionsList;
    private final double instructionsStartY;
    private final double instructionsRowGap;

    public EndScreen(Properties gameProps, boolean isWin, Player player) {
        super(gameProps);
        this.isWin = isWin;
        // Reuse the existing player so its x position is preserved into the end screen
        this.player = player;

        int textSize = Integer.parseInt(gameProps.getProperty("text.size"));
        textFont = new Font(gameProps.getProperty("text.font"), textSize);

        int winSize = Integer.parseInt(gameProps.getProperty("end.win.size"));
        winFont = new Font(gameProps.getProperty("text.font"), winSize);

        int loseSize = Integer.parseInt(gameProps.getProperty("end.lose.size"));
        loseFont = new Font(gameProps.getProperty("text.font"), loseSize);

        String[] rgb = gameProps.getProperty("text.colour").split(",");
        textColour = new Colour(
                Double.parseDouble(rgb[0]),
                Double.parseDouble(rgb[1]),
                Double.parseDouble(rgb[2])
        );

        winText = gameProps.getProperty("end.win.text");
        winY = Double.parseDouble(gameProps.getProperty("end.win.posY"));

        loseText = gameProps.getProperty("end.lose.text");
        loseY = Double.parseDouble(gameProps.getProperty("end.lose.posY"));

        instructionsList = gameProps.getProperty("end.instructionsList.text").split(",");
        instructionsStartY = Double.parseDouble(
                gameProps.getProperty("end.instructionsList.startPosY"));
        instructionsRowGap = Double.parseDouble(
                gameProps.getProperty("end.instructionsList.rowGap"));
    }

    @Override
    public ScreenAction update(Input input) {
        // Player can move but not shoot
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

        // Win or lose title — only shown for the matching state
        if (isWin) {
            double x = Window.getWidth() / 2.0 - winFont.getWidth(winText) / 2.0;
            winFont.drawString(winText, x, winY, options);
        } else {
            double x = Window.getWidth() / 2.0 - loseFont.getWidth(loseText) / 2.0;
            loseFont.drawString(loseText, x, loseY, options);
        }

        // Instructions list is shown in both win and lose states
        for (int i = 0; i < instructionsList.length; i++) {
            String line = instructionsList[i];
            double lineX = Window.getWidth() / 2.0 - textFont.getWidth(line) / 2.0;
            double lineY = instructionsStartY + instructionsRowGap * i;
            textFont.drawString(line, lineX, lineY, options);
        }
    }
}
