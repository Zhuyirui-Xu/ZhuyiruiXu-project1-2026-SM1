package game;

import bagel.Input;
import bagel.Image;

import java.util.ArrayList;
import java.util.Properties;

public class BattleScreen extends Screen {

    private Player player;
    //private ArrayList<Enemy> enemies;
    private ArrayList<Projectile> projectiles;
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
        updatePlayer(input);

        //update enemies states

        //update projectiles states
        updateProjectiles();
        //update explosions states

        //Check collision

        //Remove inactive objects
        removeInactiveObjects();


        //draw battle screen after everything is updated
        draw();
    }

    @Override
    public void draw() {

        //draw player
        player.draw();

        //draw projectiles
        for (Projectile projectile : projectiles) {
            projectile.draw();
        }


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
        int shootCooldown = Integer.parseInt(gameProps.getProperty("player.shootCooldown"));
        player = new Player(image, x, y, initialLives, speed, shootCooldown);
        //initialize enemies

        //initialize empty lists for projectiles and explosions
        projectiles = new ArrayList<>();

    }

    private void updatePlayer(Input input) {
        if (player.update(input)) {

            //Create projectile
            Projectile projectile = new Projectile(
                    new Image(gameProps.getProperty("projectile.image")),
                    player.x, player.y,
                    Double.parseDouble(gameProps.getProperty("projectile.movementSpeed")));//flaws that can be optimized here
            projectiles.add(projectile);
        }
    }

    private void updateEnemies() {

    }

    private void updateProjectiles() {
       for (Projectile projectile : projectiles) {
           projectile.update();
       }
    }

    private void updateExplosions(){

    }

    private void removeInactiveObjects() {

        //remove inactive projectiles
        for (int i = 0; i<projectiles.size(); i++) {//consider getter method
            if (!projectiles.get(i).active){
                projectiles.remove(i);
                i--;
            }
        }
    }


}
