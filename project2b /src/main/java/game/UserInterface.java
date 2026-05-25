package game;

import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.util.Colour;
import java.util.Properties;

/**
 * Renders the in-game HUD: player lives icons, current score, and current wave number.
 */
public class UserInterface {

    private final Font textFont;
    private final Colour textColour;

    private final String waveText;
    private final double waveX;
    private final double waveY;

    private final String scoreText;
    private final double scoreX;
    private final double scoreY;

    private final Image lifeIcon;
    private final double lifeX;
    private final double lifeY;
    private final double lifeGap;

    public UserInterface(Properties gameProps) {
        int textSize = Integer.parseInt(gameProps.getProperty("text.size"));
        textFont = new Font(gameProps.getProperty("text.font"), textSize);

        String[] rgb = gameProps.getProperty("text.colour").split(",");
        textColour = new Colour(
                Double.parseDouble(rgb[0]),
                Double.parseDouble(rgb[1]),
                Double.parseDouble(rgb[2])
        );

        waveText = gameProps.getProperty("wave.text");
        String[] wavePos = gameProps.getProperty("wave.pos").split(",");
        waveX = Double.parseDouble(wavePos[0]);
        waveY = Double.parseDouble(wavePos[1]);

        scoreText = gameProps.getProperty("score.text");
        String[] scorePos = gameProps.getProperty("score.pos").split(",");
        scoreX = Double.parseDouble(scorePos[0]);
        scoreY = Double.parseDouble(scorePos[1]);

        lifeIcon = new Image(gameProps.getProperty("playerLives.image"));
        String[] lifePos = gameProps.getProperty("playerLives.startPosition").split(",");
        lifeX = Double.parseDouble(lifePos[0]);
        lifeY = Double.parseDouble(lifePos[1]);
        lifeGap = Double.parseDouble(gameProps.getProperty("playerLives.gap"));
    }

    /** Draws all HUD elements. Must be called after all game entities are drawn. */
    public void draw(int lives, int score, int wave) {
        DrawOptions options = new DrawOptions().setBlendColour(textColour);
        drawLives(lives);
        textFont.drawString(scoreText + " " + score, scoreX, scoreY, options);
        textFont.drawString(waveText + " " + wave, waveX, waveY, options);
    }

    private void drawLives(int lives) {
        for (int i = 0; i < lives; i++) {
            lifeIcon.draw(lifeX + i * lifeGap, lifeY);
        }
    }
}
