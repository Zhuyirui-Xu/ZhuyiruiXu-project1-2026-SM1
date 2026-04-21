package game;

import bagel.Input;
import bagel.Image;
import bagel.Window;

import java.util.ArrayList;
import java.util.Properties;

public class BattleScreen extends Screen {

    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Projectile> projectiles;
    private ArrayList<Explosion> explosions;
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

        //initialize battle screen states
        score = 0;
        wave = 1;
        frameCount = 0;


        //Initialize HUD
        hud = new HUD(gameProps);
    }

    @Override
    public void update(Input input) {

        //update player state
        updatePlayer(input);

        //update enemies states
        updateEnemies();
        //update projectiles states
        updateProjectiles();
        //update explosions states
        updateExplosions();
        //Check collision
        checkCollisions();
        //Remove inactive objects
        removeInactiveObjects();


        //draw battle screen after everything is updated
        draw();

        //Update frameCount
        frameCount++;
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
        for (Enemy enemy : enemies) {
            enemy.update(frameCount);
        }

    }

    private void updateProjectiles() {
        for (Projectile projectile : projectiles) {
            projectile.update();
        }
    }

    private void updateExplosions(){
        for (Explosion explosion: explosions){
            explosion.update();
        }

    }

    @Override
    public void draw() {

        // draw explosions
        for (Explosion explosion:explosions){
            explosion.draw();
        }
        //draw player
        player.draw();

        //draw enemies
       for (Enemy enemy: enemies) {
           enemy.draw();
       }

        //draw projectiles
        for (Projectile projectile : projectiles) {
            projectile.draw();
        }


        //draw hud information
        hud.draw(player.getLives(), score, wave);

    }

    private void initializeGameObjects() {

        //initialize player
        createPlayer();

        //initialize enemies
        createEnemies();

        //initialize empty lists for projectiles and explosions
        projectiles = new ArrayList<>();
        explosions = new ArrayList<>();

    }

    private void createPlayer(){
        //initialize player
        Image image = new Image(gameProps.getProperty("player.image"));
        double x = ShadowAliens.screenWidth/2;
        double y = Double.parseDouble(gameProps.getProperty("player.posY"));
        int initialLives = Integer.parseInt(gameProps.getProperty("player.initialLives"));
        int speed = Integer.parseInt(gameProps.getProperty("player.speed"));
        int shootCooldown = Integer.parseInt(gameProps.getProperty("player.shootCooldown"));
        player = new Player(image, x, y, initialLives, speed, shootCooldown);
    }


    private void createEnemies() {

        enemies = new ArrayList<>();
        Image enemyImage = new Image(gameProps.getProperty("enemy.image"));

        String arrivalTimeStr = null;
        int arrivalTime;
        int speed;
        double posX;
        double posY = 0 - enemyImage.getHeight()/2;;

        int i = 0;
        while ((arrivalTimeStr = gameProps.getProperty(String.format("enemy.%d.arrivalTime", i))) != null) {

            arrivalTime = Integer.parseInt(arrivalTimeStr);
            speed = Integer.parseInt(gameProps.getProperty(String.format("enemy.%d.movementSpeed", i)));
            posX = Double.parseDouble(gameProps.getProperty(String.format("enemy.%d.posX", i)));

            Enemy enemy = new Enemy(enemyImage, posX, posY, arrivalTime, speed);
            enemies.add(enemy);
            i++;
        }
    }


    private void checkCollisions() {
        checkEnemyPlayerCollision();
        checkEnemyProjectileCollision();
    }

    private void checkEnemyPlayerCollision(){
        for (Enemy enemy: enemies) {

            if(enemy.isActive() && enemy.hasArrived(frameCount)) {
                if (enemy.collideWith(player)) {

                    //enemy destroyed
                    enemy.destroy();

                    //player lose life
                    player.loseLife();

                    if (player.isDead()){
                        Window.close();
                    }
                }
            }
        }
    }

    private void checkEnemyProjectileCollision(){
        for (Enemy enemy: enemies) {

            if(enemy.isActive() && enemy.hasArrived(frameCount)) {
                for(Projectile projectile: projectiles) {
                    if(projectile.isActive()) {
                        if (enemy.collideWith(projectile)) {
                            //enemy destroyed
                            enemy.destroy();
                            //projectile destroyed
                            projectile.destroy();
                            //score
                            score(1);
                            //create explosion
                            Explosion explosion = new Explosion(new Image(gameProps.getProperty("explosion.image")),
                                    enemy.x,
                                    enemy.y,
                                    Integer.parseInt(gameProps.getProperty("explosion.duration")));
                            explosions.add(explosion);
                        }
                    }
                }
            }
        }
    }


    private void removeInactiveObjects() {

        //remove inactive enemies
        for (int i = 0; i<enemies.size(); i++) {//consider getter method
            if (!enemies.get(i).isActive()) {
                enemies.remove(i);
                i--;
            }
        }


        //remove inactive projectiles
        for (int i = 0; i<projectiles.size(); i++) {//consider getter method
            if (!projectiles.get(i).isActive()){
                projectiles.remove(i);
                i--;
            }
        }

        //remove inactive explosions
        for (int i = 0; i<explosions.size(); i++) {//consider getter method
            if (!explosions.get(i).isActive()){
                explosions.remove(i);
                i--;
            }
        }
    }

    private void score(int enemyScore){
        score += enemyScore;
    }
}
