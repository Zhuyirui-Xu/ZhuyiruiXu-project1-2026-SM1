package game;

import bagel.Image;
import bagel.Input;
import bagel.Keys;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Battle screen: the main gameplay loop.
 * Manages waves, the player, all enemies, projectiles, power-ups, and explosions.
 * Handles all collision detection, scoring, dev-mode controls, and wave progression.
 */
public class BattleScreen extends ScreenState {

    private Player player;
    private final ArrayList<Enemy> enemyList;
    private final ArrayList<EnemyProjectile> enemyProjectileList;
    private final ArrayList<PowerUp> powerupList;
    private final ArrayList<Projectile> projectileList;
    private final ArrayList<Explosion> explosionList;

    private int score;
    private int currentWaveNumber;
    private Wave currentWave;
    private final int totalWaves;

    // Dev-mode speed control: 0 = normal, positive = faster, negative = slower
    private int speedLevel;

    private final UserInterface userInterface;

    // Cached images and values loaded once at construction
    private final Image playerProjectileImage;
    private final double playerProjectileSpeed;

    private final Image explosionLargeImage;
    private final int explosionLargeDuration;
    private final Image explosionSmallImage;
    private final int explosionSmallDuration;

    // Score values from properties
    private final int scoreDestroyedRegular;
    private final int scoreDestroyedStrafing;
    private final int scoreDestroyedShooting;
    private final int scoreGotHit;
    private final int scoreGotPowerup;
    private final int scoreHitProjectile;
    private final int scoreWaveCompleted;

    private final Properties gameProps;

    public BattleScreen(Properties gameProps) {
        super(gameProps);
        this.gameProps = gameProps;

        // Determine how many waves exist in the properties file
        int waveCount = 0;
        while (gameProps.containsKey("wave." + (waveCount + 1) + ".enemy.0.arrivalTime")) {
            waveCount++;
        }
        totalWaves = waveCount;

        // Cache images
        playerProjectileImage = new Image(gameProps.getProperty("projectile.image"));
        playerProjectileSpeed = Double.parseDouble(gameProps.getProperty("projectile.movementSpeed"));

        explosionLargeImage = new Image(gameProps.getProperty("explosion.large.image"));
        explosionLargeDuration = Integer.parseInt(gameProps.getProperty("explosion.large.duration"));
        explosionSmallImage = new Image(gameProps.getProperty("explosion.small.image"));
        explosionSmallDuration = Integer.parseInt(gameProps.getProperty("explosion.small.duration"));

        // Score values
        scoreDestroyedRegular = Integer.parseInt(gameProps.getProperty("score.destroyedEnemy.regular"));
        scoreDestroyedStrafing = Integer.parseInt(gameProps.getProperty("score.destroyedEnemy.strafing"));
        scoreDestroyedShooting = Integer.parseInt(gameProps.getProperty("score.destroyedEnemy.shooting"));
        scoreGotHit = Integer.parseInt(gameProps.getProperty("score.gotHit"));
        scoreGotPowerup = Integer.parseInt(gameProps.getProperty("score.gotPowerup"));
        scoreHitProjectile = Integer.parseInt(gameProps.getProperty("score.hitProjectile"));
        scoreWaveCompleted = Integer.parseInt(gameProps.getProperty("score.waveCompleted"));

        enemyList = new ArrayList<>();
        enemyProjectileList = new ArrayList<>();
        powerupList = new ArrayList<>();
        projectileList = new ArrayList<>();
        explosionList = new ArrayList<>();

        score = 0;
        speedLevel = 0;

        player = createPlayer();
        userInterface = new UserInterface(gameProps);

        currentWaveNumber = 1;
        currentWave = new Wave(gameProps, currentWaveNumber);
    }

    /** Returns the player reference so the Pause screen can share it for display. */
    public Player getPlayer() {
        return player;
    }

    @Override
    public ScreenAction update(Input input) {
        ScreenAction devAction = handleDevControls(input);
        if (devAction != ScreenAction.NONE) return devAction;

        if (input.wasPressed(Keys.ESCAPE)) {
            return ScreenAction.PAUSE;
        }

        double ts = computeTimeScale();

        // Spawn new entities scheduled for this frame
        enemyList.addAll(currentWave.createEnemies());
        powerupList.addAll(currentWave.createPowerUps());

        updatePlayer(input, ts);
        updateEnemies(ts);
        updateEnemyProjectiles(ts);
        updatePowerUps(ts);
        updatePlayerProjectiles(ts);
        updateExplosions(ts);

        checkAllCollisions();
        removeInactiveObjects();

        currentWave.updateFrame(ts);

        // Check if wave is complete (all spawned and all destroyed/off-screen)
        if (currentWave.allSpawned()
                && enemyList.isEmpty()
                && enemyProjectileList.isEmpty()
                && powerupList.isEmpty()) {
            return advanceWave();
        }

        // Check lose condition
        if (player.isDead()) {
            return ScreenAction.LOSE;
        }

        return ScreenAction.NONE;
    }

    @Override
    public void draw() {

        for (Explosion e : explosionList) e.draw();
        for (PowerUp p : powerupList) p.draw();
        for (Enemy e : enemyList) e.draw();
        player.draw();

        for (EnemyProjectile ep : enemyProjectileList) ep.draw();
        for (Projectile p : projectileList) p.draw();

        userInterface.draw(player.getLives(), score, currentWaveNumber);
    }


    private ScreenAction handleDevControls(Input input) {
        if (input.wasPressed(Keys.G)) speedLevel++;
        if (input.wasPressed(Keys.F)) speedLevel--;
        if (input.wasPressed(Keys.I)) player.toggleDevInvincible();

        if (input.wasPressed(Keys.R)) {
            return ScreenAction.RESTART_START;
        }

        if (input.wasPressed(Keys.N)) {
            return skipToNextWave();
        }

        return ScreenAction.NONE;
    }

    /**
     * Skips the current wave: destroys all on-screen entities without score or explosions,
     * awards wave-completed score, then starts the next wave (or triggers a win).
     */
    private ScreenAction skipToNextWave() {
        // Destroy everything silently
        for (Enemy e : enemyList) e.destroy();
        for (EnemyProjectile ep : enemyProjectileList) ep.destroy();
        for (Projectile p : projectileList) p.destroy();
        for (PowerUp p : powerupList) p.destroy();

        score += scoreWaveCompleted;
        removeInactiveObjects();

        if (currentWaveNumber >= totalWaves) {
            return ScreenAction.WIN;
        }
        currentWaveNumber++;
        currentWave = new Wave(gameProps, currentWaveNumber);
        return ScreenAction.NONE;
    }

    private ScreenAction advanceWave() {
        score += scoreWaveCompleted;
        if (currentWaveNumber >= totalWaves) {
            return ScreenAction.WIN;
        }
        currentWaveNumber++;
        currentWave = new Wave(gameProps, currentWaveNumber);
        return ScreenAction.NONE;
    }


    private void updatePlayer(Input input, double ts) {
        boolean fired = player.update(input, ts);
        if (fired) {
            projectileList.add(new Projectile(
                    playerProjectileImage, player.getX(), player.getY(), playerProjectileSpeed));
        }
    }

    private void updateEnemies(double ts) {
        for (Enemy e : enemyList) {
            e.update(ts);
            // Collect any projectiles fired by shooting enemies this frame
            if (e instanceof ShootingEnemy se) {
                enemyProjectileList.addAll(se.collectProjectiles());
            }
        }
    }

    private void updateEnemyProjectiles(double ts) {
        for (EnemyProjectile ep : enemyProjectileList) ep.update(ts);
    }

    private void updatePowerUps(double ts) {
        for (PowerUp p : powerupList) p.update(ts);
    }

    private void updatePlayerProjectiles(double ts) {
        for (Projectile p : projectileList) p.update(ts);
    }

    private void updateExplosions(double ts) {
        for (Explosion e : explosionList) e.update(ts);
    }



    private void checkAllCollisions() {
        checkPlayerVsEnemies();
        checkPlayerVsEnemyProjectiles();
        checkPlayerVsPowerUps();
        checkPlayerProjectilesVsEnemies();
        checkPlayerProjectilesVsEnemyProjectiles();
    }

    /** Player collides with an enemy ship: enemy destroyed, player loses life (unless invincible). */
    private void checkPlayerVsEnemies() {
        for (Enemy e : enemyList) {
            if (!e.isActive()) continue;
            if (e.collideWith(player)) {
                e.destroy();
                // Large explosion at enemy position
                explosionList.add(new Explosion(explosionLargeImage, e.getX(), e.getY(), explosionLargeDuration));

                if (!player.isInvincible()) {
                    score = Math.max(0, score - scoreGotHit);
                    player.loseLife();
                }
            }
        }
    }

    /** Player collides with an enemy projectile: projectile destroyed, player loses life (unless invincible). */
    private void checkPlayerVsEnemyProjectiles() {
        for (EnemyProjectile ep : enemyProjectileList) {
            if (!ep.isActive()) continue;
            if (ep.collideWith(player)) {
                ep.destroy();
                // Small explosion at projectile position
                explosionList.add(new Explosion(explosionSmallImage, ep.getX(), ep.getY(), explosionSmallDuration));

                if (!player.isInvincible()) {
                    score = Math.max(0, score - scoreGotHit);
                    player.loseLife();
                }
            }
        }
    }

    /** Player collides with a power-up: apply effect, award score, remove power-up. */
    private void checkPlayerVsPowerUps() {
        for (PowerUp p : powerupList) {
            if (!p.isActive()) continue;
            if (p.collideWith(player)) {
                p.applyEffect(player);
                p.destroy();
                score += scoreGotPowerup;
            }
        }
    }

    /** Player projectile hits an enemy: both destroyed, score awarded, large explosion created. */
    private void checkPlayerProjectilesVsEnemies() {
        for (Enemy e : enemyList) {
            if (!e.isActive()) continue;
            for (Projectile p : projectileList) {
                if (!p.isActive()) continue;
                if (e.collideWith(p)) {
                    e.destroy();
                    p.destroy();
                    score += scoreForEnemy(e);
                    explosionList.add(new Explosion(explosionLargeImage, e.getX(), e.getY(), explosionLargeDuration));
                    break;
                }
            }
        }
    }

    /** Player projectile hits an enemy projectile: both destroyed, small explosion, score awarded. */
    private void checkPlayerProjectilesVsEnemyProjectiles() {
        for (EnemyProjectile ep : enemyProjectileList) {
            if (!ep.isActive()) continue;
            for (Projectile p : projectileList) {
                if (!p.isActive()) continue;
                if (ep.collideWith(p)) {
                    ep.destroy();
                    p.destroy();
                    score += scoreHitProjectile;
                    explosionList.add(new Explosion(explosionSmallImage, ep.getX(), ep.getY(), explosionSmallDuration));
                    break;
                }
            }
        }
    }

    private int scoreForEnemy(Enemy enemy) {
        if (enemy instanceof StrafingEnemy) return scoreDestroyedStrafing;
        if (enemy instanceof ShootingEnemy) return scoreDestroyedShooting;
        return scoreDestroyedRegular;
    }


    private void removeInactiveObjects() {
        enemyList.removeIf(e -> !e.isActive());
        enemyProjectileList.removeIf(ep -> !ep.isActive());
        powerupList.removeIf(p -> !p.isActive());
        projectileList.removeIf(p -> !p.isActive());
        explosionList.removeIf(e -> !e.isActive());
    }



    public double computeTimeScale() {
        if (speedLevel > 0) return speedLevel + 1.0;
        if (speedLevel < 0) return 1.0 / (-speedLevel + 1.0);
        return 1.0;
    }

    public String getTimescaleDisplay() {
        if (speedLevel >= 0) {
            return String.valueOf((int) computeTimeScale());
        }
        return "1/" + (-speedLevel + 1);
    }


    public void increaseSpeed() { speedLevel++; }
    public void decreaseSpeed() { speedLevel--; }
    public ScreenAction skipWave() {
        return skipToNextWave();
    }
}
