package game;

import bagel.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Manages the spawn schedule for enemies and power-ups in a single numbered wave.
 * Each frame, createEnemies() and createPowerUps() are called; they return any
 * entities whose arrivalTime has been reached this frame.
 */
public class Wave {

    private final int waveNumber;
    private double scaledFrameCount = 0.0;


    private final List<EnemySpawnData> enemySpawns;
    private final List<PowerUpSpawnData> powerUpSpawns;


    private final Image regularImage;
    private final Image strafingImage;
    private final Image shootingImage;
    private final Image enemyProjectileImage;
    private final double enemyProjectileSpeed;
    private final int firingRate;

    private final Image shieldImage;
    private final Image lifeImage;
    private final Image cooldownImage;
    private final Image engineImage;
    private final int shieldDuration;
    private final int cooldownDuration;
    private final int engineDuration;
    private final double shieldSpeed;
    private final double lifeSpeed;
    private final double cooldownSpeed;
    private final double engineSpeed;

    private final double screenWidth;
    private final double screenHeight;


    private final boolean[] enemySpawned;
    private final boolean[] powerUpSpawned;

    public Wave(Properties props, int waveNumber) {
        this.waveNumber = waveNumber;
        this.scaledFrameCount = 0.0;
        this.enemySpawns = new ArrayList<>();
        this.powerUpSpawns = new ArrayList<>();

        screenWidth = Double.parseDouble(props.getProperty("window.width"));
        screenHeight = Double.parseDouble(props.getProperty("window.height"));


        regularImage = new Image(props.getProperty("enemy.regular.image"));
        strafingImage = new Image(props.getProperty("enemy.strafing.image"));
        shootingImage = new Image(props.getProperty("enemy.shooting.image"));
        enemyProjectileImage = new Image(props.getProperty("enemyProjectile.image"));
        enemyProjectileSpeed = Double.parseDouble(props.getProperty("enemyProjectile.movementSpeed"));
        firingRate = Integer.parseInt(props.getProperty("enemy.shooting.firingRate"));


        shieldImage = new Image(props.getProperty("powerup.shield.image"));
        lifeImage = new Image(props.getProperty("powerup.life.image"));
        cooldownImage = new Image(props.getProperty("powerup.cooldown.image"));
        engineImage = new Image(props.getProperty("powerup.engine.image"));

        shieldDuration = Integer.parseInt(props.getProperty("powerup.shield.duration"));
        cooldownDuration = Integer.parseInt(props.getProperty("powerup.cooldown.duration"));
        engineDuration = Integer.parseInt(props.getProperty("powerup.engine.duration"));

        shieldSpeed = Double.parseDouble(props.getProperty("powerup.shield.movementSpeed"));
        lifeSpeed = Double.parseDouble(props.getProperty("powerup.life.movementSpeed"));
        cooldownSpeed = Double.parseDouble(props.getProperty("powerup.cooldown.movementSpeed"));
        engineSpeed = Double.parseDouble(props.getProperty("powerup.engine.movementSpeed"));


        String prefix = "wave." + waveNumber + ".enemy.";
        for (int i = 0; props.containsKey(prefix + i + ".arrivalTime"); i++) {
            int arrivalTime = Integer.parseInt(props.getProperty(prefix + i + ".arrivalTime"));
            double posX = Double.parseDouble(props.getProperty(prefix + i + ".posX"));
            int speed = Integer.parseInt(props.getProperty(prefix + i + ".movementSpeed"));
            String type = props.getProperty(prefix + i + ".type");
            enemySpawns.add(new EnemySpawnData(arrivalTime, posX, speed, type));
        }


        String puPrefix = "wave." + waveNumber + ".powerup.";
        for (int i = 0; props.containsKey(puPrefix + i + ".arrivalTime"); i++) {
            int arrivalTime = Integer.parseInt(props.getProperty(puPrefix + i + ".arrivalTime"));
            double posX = Double.parseDouble(props.getProperty(puPrefix + i + ".posX"));
            String type = props.getProperty(puPrefix + i + ".type");
            powerUpSpawns.add(new PowerUpSpawnData(arrivalTime, posX, type));
        }

        enemySpawned = new boolean[enemySpawns.size()];
        powerUpSpawned = new boolean[powerUpSpawns.size()];
    }


    public void updateFrame(double timeScale) {
        scaledFrameCount += timeScale;
    }


    public List<Enemy> createEnemies() {
        List<Enemy> newEnemies = new ArrayList<>();
        for (int i = 0; i < enemySpawns.size(); i++) {
            if (!enemySpawned[i] && scaledFrameCount >= enemySpawns.get(i).arrivalTime) {
                newEnemies.add(buildEnemy(enemySpawns.get(i)));
                enemySpawned[i] = true;
            }
        }
        return newEnemies;
    }


    public List<PowerUp> createPowerUps() {
        List<PowerUp> newPowerUps = new ArrayList<>();
        for (int i = 0; i < powerUpSpawns.size(); i++) {
            if (!powerUpSpawned[i] && scaledFrameCount >= powerUpSpawns.get(i).arrivalTime) {
                newPowerUps.add(buildPowerUp(powerUpSpawns.get(i)));
                powerUpSpawned[i] = true;
            }
        }
        return newPowerUps;
    }

    public boolean allSpawned() {
        for (boolean b : enemySpawned) if (!b) return false;
        for (boolean b : powerUpSpawned) if (!b) return false;
        return true;
    }


    private Enemy buildEnemy(EnemySpawnData data) {
        return switch (data.type) {
            case "strafing" -> new StrafingEnemy(strafingImage, data.posX, data.speed,
                    screenHeight, screenWidth);
            case "shooting" -> new ShootingEnemy(shootingImage, data.posX, data.speed,
                    screenHeight, firingRate, enemyProjectileImage, enemyProjectileSpeed);
            default -> new RegularEnemy(regularImage, data.posX, data.speed, screenHeight);
        };
    }

    private PowerUp buildPowerUp(PowerUpSpawnData data) {
        return switch (data.type) {
            case "shield" -> new ShieldPowerUp(shieldImage, data.posX, shieldSpeed,
                    screenHeight, shieldDuration);
            case "life" -> new LifePowerUp(lifeImage, data.posX, lifeSpeed, screenHeight);
            case "cooldown" -> new CooldownPowerUp(cooldownImage, data.posX, cooldownSpeed,
                    screenHeight, cooldownDuration);
            case "engine" -> new EnginePowerUp(engineImage, data.posX, engineSpeed,
                    screenHeight, engineDuration);
            default -> new LifePowerUp(lifeImage, data.posX, lifeSpeed, screenHeight);
        };
    }



    private static class EnemySpawnData {
        final int arrivalTime;
        final double posX;
        final int speed;
        final String type;

        EnemySpawnData(int arrivalTime, double posX, int speed, String type) {
            this.arrivalTime = arrivalTime;
            this.posX = posX;
            this.speed = speed;
            this.type = type;
        }
    }

    private static class PowerUpSpawnData {
        final int arrivalTime;
        final double posX;
        final String type;

        PowerUpSpawnData(int arrivalTime, double posX, String type) {
            this.arrivalTime = arrivalTime;
            this.posX = posX;
            this.type = type;
        }
    }
}
