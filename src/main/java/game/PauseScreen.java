package game;

import bagel.Font;
import bagel.Input;
import bagel.util.Colour;

import java.util.Properties;

public class PauseScreen extends Screen {

    private BattleScreen battleScreen;
    private HUD hud;

    private int textSize;
    private Font textFont;
    private Colour textColour;

    private String pauseTitle;
    private double titleY;
    private int titleFontSize;

    private String[] controlList;
    private double controlsStartY;
    private double controlsLineGap;

    private String speedPrefix;
    private double speedX;
    private double speedY;


    public PauseScreen(Properties gameProps, BattleScreen battleScreen) {
        super(gameProps);
        this.battleScreen = battleScreen;
        this.hud = new HUD(gameProps);

        //Initialize attributes for UI drawing: refer to HUD
        //Pause screen settings
        textSize = Integer.parseInt(gameProps.getProperty("text.size"));
        textFont = new Font(gameProps.getProperty("text.font"), textSize);

        String[] colorParts = gameProps.getProperty("text.colour").split(",");
        double r = Double.parseDouble(colorParts[0]);
        double g = Double.parseDouble(colorParts[1]);
        double b = Double.parseDouble(colorParts[2]);
        textColour = new Colour(r, g, b);

        pauseTitle = gameProps.getProperty("pausedTitle.text");
        titleY = Double.parseDouble(gameProps.getProperty("pausedTitle.posY"));
        titleFontSize = (int) Double.parseDouble(gameProps.getProperty("pausedTitle.size"));

        controlList = gameProps.getProperty("controlsList.text").split(",");
        controlsStartY = Double.parseDouble(gameProps.getProperty("controlsList.startPosY"));
        controlsLineGap = Double.parseDouble(gameProps.getProperty("controlsList.rowGap"));

        speedPrefix = gameProps.getProperty("timescale.text");
        String[] speedPos = gameProps.getProperty("timescale.pos").split(",");
        speedX = Double.parseDouble(speedPos[0]);
        speedY = Double.parseDouble(speedPos[1]);
    }

    @Override
    public void update(Input input) {
        draw();
    }

    @Override
    public void draw() {
        battleScreen.draw();
        drawInformation();
    }

    private void drawInformation(){
        //title:centred text -> calculate correct x position
        //screenWidth/2 - font.getWidth()/2
        Font titleFont = new Font(gameProps.getProperty("text.font"), titleFontSize);
        double titleX = ShadowAliens.screenWidth / 2.0 - titleFont.getWidth(pauseTitle) / 2.0;
        titleFont.drawString(pauseTitle, titleX, titleY);

        //control list;centred text
        for (int i = 0; i < controlList.length; i++) {
            String text = controlList[i].trim();
            double textX = ShadowAliens.screenWidth / 2.0 - textFont.getWidth(text) / 2.0;
            double textY = controlsStartY + i * controlsLineGap;
            textFont.drawString(text, textX, textY);
        }

        //timescale
        double timeScale = battleScreen.computeTimescale();
        String speedText = String.format("%s%.1f", speedPrefix, timeScale);
        textFont.drawString(speedText, speedX, speedY);
    }
}
