package game;

import bagel.Input;
import bagel.Image;

import java.util.ArrayList;
import java.util.Properties;

public class BattleScreen extends Screen {

    private Player player;
    //private ArrayList<Enemy> enemies;
    //private ArrayList<Projectile> projectiles;
    //private ArrayList<Explosion> explosions;
    //battle screen status
    private int score;
    private int wave;
    private int frameCount;

    //UI settings
    private HUD hud;//delegation

    public BattleScreen(Properties gameProps) {
        super(gameProps);

        //Initialize battle screen objects
        initializeGameObjects();

        //Initialize HUD
        hud = new HUD(gameProps);
    }

    @Override
    public void update(Input input) {

        //update player state
        player.update(input);
        //update enemies states

        //update projectiles states

        //update explosions states


        //draw battle screen after everything is updated
        draw();
    }

    @Override
    public void draw() {

        //draw player
        player.draw();

        //draw hud information
        hud.draw(player.getLives(), score, wave);

    }

    private void initializeGameObjects() {
        //initialize player
        Image image = new Image(gameProps.getProperty("player.image"));
        double x = ShadowAliens.screenWidth/2;
        double y = Double.parseDouble(gameProps.getProperty("player.posY"));
        int initialLives = Integer.parseInt(gameProps.getProperty("player.initialLives"));
        int speed = Integer.parseInt(gameProps.getProperty("player.speed"));
        player = new Player(image, x, y, initialLives, speed);
        //initialize enemies

        //initialize empty lists for projectiles and explosions
    }
}
