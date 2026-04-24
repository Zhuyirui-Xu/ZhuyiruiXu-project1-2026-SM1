package game;
import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.util.Colour;
import java.util.Properties;

/**
 * Renders in-game UI elements including lives, score, and wave information.
 */
public class UserInterface {
    private int textSize;
    private Font textFont;
    private Colour textColour;

    private String waveText;
    private double waveX, waveY;

    private String scoreText;
    private double scoreX, scoreY;

    private Image lifeIcon;
    private double lifeX, lifeY, lifeGap;

    public UserInterface(Properties gameProps) {
        initFont(gameProps);
        initWave(gameProps);
        initScore(gameProps);
        initLives(gameProps);
    }


    private void initFont(Properties gameProps) {
        textSize = Integer.parseInt(gameProps.getProperty("text.size"));
        textFont = new Font(gameProps.getProperty("text.font"), textSize);
        String[] rgb = gameProps.getProperty("text.colour").split(",");
        textColour = new Colour(
                Double.parseDouble(rgb[0]),
                Double.parseDouble(rgb[1]),
                Double.parseDouble(rgb[2])
        );
    }

    private void initWave(Properties gameProps) {
        waveText = gameProps.getProperty("wave.text");
        String[] pos = gameProps.getProperty("wave.pos").split(",");
        waveX = Double.parseDouble(pos[0]);
        waveY = Double.parseDouble(pos[1]);
    }

    private void initScore(Properties gameProps) {
        scoreText = gameProps.getProperty("score.text");
        String[] pos = gameProps.getProperty("score.pos").split(",");
        scoreX = Double.parseDouble(pos[0]);
        scoreY = Double.parseDouble(pos[1]);
    }

    private void initLives(Properties gameProps) {
        lifeIcon = new Image(gameProps.getProperty("playerLives.image"));
        String[] pos = gameProps.getProperty("playerLives.startPosition").split(",");
        lifeX = Double.parseDouble(pos[0]);
        lifeY = Double.parseDouble(pos[1]);
        lifeGap = Double.parseDouble(gameProps.getProperty("playerLives.gap"));
    }

    public void draw(int lives, int score, int wave) {
        drawLives(lives);
        drawScore(score);
        drawWave(wave);
    }


    private void drawLives(int lives) {
        for (int i = 0; i < lives; i++) {
            // Space out life icons horizontally using the defined gap value
            lifeIcon.draw(lifeX + i * lifeGap, lifeY);
        }
    }

    private void drawScore(int score) {
        textFont.drawString(scoreText + " " + score, scoreX, scoreY, new DrawOptions().setBlendColour(textColour));
    }

    private void drawWave(int wave) {
        textFont.drawString(waveText + " " + wave, waveX, waveY, new DrawOptions().setBlendColour(textColour));
    }
}
