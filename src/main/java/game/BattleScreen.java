package game;

import bagel.Input;
import bagel.Image;
import bagel.Window;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Main gameplay screen where player movement, shooting, collisions, and scoring occur.
 */
public class BattleScreen extends ScreenState {
    private Player player;
    private ArrayList<Enemy> enemyList;
    private ArrayList<Projectile> projectileList;
    private ArrayList<Explosion> explosionList;

    private int score;
    private int waveNumber;
    private int frameCounter;

    private boolean isInvincible;
    private int speedLevel;

    private UserInterface userInterface;


    private Image projectileImage;
    private double projectileSpeed;

    private Image explosionImage;
    private int explosionDuration;

    private Image enemyImage;

    public BattleScreen(Properties gameProps) {
        super(gameProps);

        cacheResources();
        initializeGameObjects();

        score = 0;
        waveNumber = 1;
        frameCounter = 0;
        isInvincible = false;
        speedLevel = 0;

        userInterface = new UserInterface(gameProps);
    }

    // Load images once at start to avoid repeated I/O operations
    private void cacheResources() {
        projectileImage = new Image(gameProps.getProperty("projectile.image"));
        projectileSpeed = Double.parseDouble(gameProps.getProperty("projectile.movementSpeed"));

        explosionImage = new Image(gameProps.getProperty("explosion.image"));
        explosionDuration = Integer.parseInt(gameProps.getProperty("explosion.duration"));

        enemyImage = new Image(gameProps.getProperty("enemy.image"));
    }

    @Override
    public void update(Input input) {

        updatePlayer(input);
        updateEnemies();

        updateProjectiles();
        updateExplosions();

        checkAllCollisions();
        removeInactiveObjects();

        draw();
        frameCounter++;
    }


    private void updatePlayer(Input input) {
        boolean shooting = player.update(input, computeTimeScale());
        if (shooting) {
            projectileList.add(new Projectile(projectileImage, player.getX(), player.getY(), projectileSpeed));
        }
    }

    private void updateEnemies() {
        for (Enemy e : enemyList) {
            e.update(frameCounter, computeTimeScale());
        }
    }

    private void updateProjectiles() {
        for (Projectile p : projectileList) {
            p.update(computeTimeScale());
        }
    }

    private void updateExplosions() {
        for (Explosion e : explosionList) {
            e.update(computeTimeScale());
        }
    }

    @Override
    public void draw() {
        player.draw();

        for (Enemy e : enemyList) e.draw();
        for (Projectile p : projectileList) p.draw();

        for (Explosion e : explosionList) e.draw();

        userInterface.draw(player.getLives(), score, waveNumber);
    }

    private void initializeGameObjects() {
        createPlayer();
        createEnemies();
        projectileList = new ArrayList<>();
        explosionList = new ArrayList<>();
    }

    private void createPlayer() {
        Image img = new Image(gameProps.getProperty("player.image"));
        double x = ShadowAliens.screenWidth / 2;
        double y = Double.parseDouble(gameProps.getProperty("player.posY"));
        int lives = Integer.parseInt(gameProps.getProperty("player.initialLives"));
        int speed = Integer.parseInt(gameProps.getProperty("player.speed"));
        int cooldown = Integer.parseInt(gameProps.getProperty("player.shootCooldown"));
        player = new Player(img, x, y, lives, speed, cooldown);
    }

    // Load enemies dynamically until no more configuration entries are found
    private void createEnemies() {
        enemyList = new ArrayList<>();

        double startY = -enemyImage.getHeight() / 2;

        for (int i = 0; ; i++) {

            String key = "enemy." + i + ".arrivalTime";
            String arrivalStr = gameProps.getProperty(key);

            if (arrivalStr == null) break;

            int arrival = Integer.parseInt(arrivalStr);
            int speed = Integer.parseInt(gameProps.getProperty("enemy." + i + ".movementSpeed"));
            double x = Double.parseDouble(gameProps.getProperty("enemy." + i + ".posX"));

            enemyList.add(new Enemy(enemyImage, x, startY, arrival, speed));
        }
    }

    private void checkAllCollisions() {
        checkPlayerEnemy();
        checkProjectileEnemy();
    }


    private void checkPlayerEnemy() {
        for (Enemy e : enemyList) {
            if (e.isActive() && e.hasArrived(frameCounter) && e.collideWith(player)) {
                e.destroy();

                if (!isInvincible) {
                    player.loseLife();
                }

                if (player.isDead()) {
                    Window.close();
                }
            }
        }
    }


    private void checkProjectileEnemy() {
        for (Enemy e : enemyList) {
            if (!e.isActive() || !e.hasArrived(frameCounter)) continue;

            for (Projectile p : projectileList) {
                if (p.isActive() && e.collideWith(p)) {
                    e.destroy();
                    p.destroy();
                    score++;
                    explosionList.add(new Explosion(explosionImage, e.getX(), e.getY(), explosionDuration));
                    break;
                }
            }
        }
    }


    private void removeInactiveObjects() {
        enemyList.removeIf(e -> !e.isActive());
        projectileList.removeIf(p -> !p.isActive());
        explosionList.removeIf(e -> !e.isActive());
    }


    public double computeTimeScale() {
        if (speedLevel > 0) return speedLevel + 1;
        if (speedLevel < 0) return 1.0 / (-speedLevel + 1);
        return 1.0;
    }

    public void increaseSpeedLevel() { speedLevel++; }
    public void decreaseSpeedLevel() { speedLevel--; }
    public void toggleInvincibleMode() { isInvincible = !isInvincible; }
}