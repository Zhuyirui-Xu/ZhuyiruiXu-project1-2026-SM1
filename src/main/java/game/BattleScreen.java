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

    public BattleScreen(Properties gameProps) {
        super(gameProps);

        //Initialize battle screen objects
        initializeGameObjects();
    }

    @Override
    public void update(Input input) {


        //draw battle screen after everything is updated
        draw();
    }

    @Override
    public void draw() {

        //draw player
        player.draw();

    }

    private void initializeGameObjects() {
        //initialize player
        Image image = new Image(gameProps.getProperty("player.image"));
        double x = ShadowAliens.screenWidth/2;
        double y = Double.parseDouble(gameProps.getProperty("player.posY"));
        player = new Player(image, x, y, true);
        //initialize enemies

        //initialize empty lists for projectiles and explosions
    }
}
