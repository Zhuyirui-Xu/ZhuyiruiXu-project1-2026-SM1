package game;

import bagel.DrawOptions;
import bagel.util.Colour;

import java.awt.*;
import bagel.Image;
import bagel.Font;
import java.util.Properties;

public class HUD {

    private int textSize;
    private Font textFont;
    private Colour textColour;

    private String waveText;
    private double waveX;
    private double waveY;

    private String scoreText;
    private double scoreX;
    private double scoreY;

    private Image playerLivesImage;
    private double playerLivesStartPosX;
    private double playerLivesStartPosY;
    private double playerLivesGap;

    public HUD(Properties gameProps) {
        textSize = Integer.parseInt(gameProps.getProperty("text.size"));
        textFont = new Font(gameProps.getProperty("text.font"), textSize);

        String[] textColours = gameProps.getProperty("text.colour").split(",");
        double r = Double.parseDouble(textColours[0]);
        double g = Double.parseDouble(textColours[1]);
        double b = Double.parseDouble(textColours[2]);
        textColour = new Colour(r, g, b);

        playerLivesImage = new Image(gameProps.getProperty("playerLives.image"));
        String[] playerLivesStartPos = gameProps.getProperty("playerLives.startPosition").split(",");
        playerLivesStartPosX = Double.parseDouble(playerLivesStartPos[0]);
        playerLivesStartPosY = Double.parseDouble(playerLivesStartPos[1]);
        playerLivesGap = Double.parseDouble(gameProps.getProperty("playerLives.gap"));


        waveText =  gameProps.getProperty("wave.text");
        String[] wavePos = gameProps.getProperty("wave.pos").split(",");//can use helper methods here
        waveX = Double.parseDouble(wavePos[0]);
        waveY = Double.parseDouble(wavePos[1]);

        scoreText = gameProps.getProperty("score.text");
        String[] scorePos = gameProps.getProperty("score.pos").split(",");
        scoreX = Double.parseDouble(scorePos[0]);
        scoreY = Double.parseDouble(scorePos[1]);
    }

    public void draw(int lives, int score, int wave){

        //draw lives images
        drawLivesImage(lives);
        //draw score
        drawScore(score);

        //draw wave
        drawWave(wave);

        //drawString  needs to draw the string's left bottom coordinates; Absolute position can just use it

    }

    private void drawLivesImage(int lives){

        for(int i = 0; i < lives; i++){
            playerLivesImage.draw(playerLivesStartPosX + i * playerLivesGap, playerLivesStartPosY);
        }

    }

    private void drawScore(int score){
        textFont.drawString(
                String.format("%s %d", scoreText, score),
                scoreX,
                scoreY,
                new DrawOptions().setBlendColour(textColour)
        );
    }

    private void drawWave(int wave){
        textFont.drawString(
                String.format("%s %d", waveText, wave),
                waveX,
                waveY,
                new DrawOptions().setBlendColour(textColour)
        );
    }
}
