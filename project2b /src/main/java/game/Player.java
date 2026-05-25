package game;

import bagel.Image;
import bagel.Input;
import bagel.Keys;

/**
 * Represents the player's ship. Handles horizontal movement, shooting with cooldown,
 * lives management, and power-up effects (shield, cooldown boost, engine boost).
 * Draws an invincibility overlay when shielded by either a power-up or dev-mode toggle.
 */
public class Player extends GameObject {

    private final double screenWidth;
    private final double posY;
    private final int baseSpeed;
    private final int baseCooldown;
    private final int initialLives;
    private final Image invincibilityImage;

    private int currentLives;

    private double cooldownTimer;


    private double shieldTimer;
    private double cooldownBoostTimer;
    private double engineBoostTimer;


    private boolean devInvincible;


    private final int hitInvincibilityTime;
    private double hitInvincibilityTimer;

    public Player(Image image, Image invincibilityImage, double startX, double posY,
                  int speed, int shootCooldown, int initialLives,
                  double screenWidth, int hitInvincibilityTime) {
        super(image, startX, posY);
        this.invincibilityImage = invincibilityImage;
        this.posY = posY;
        this.baseSpeed = speed;
        this.baseCooldown = shootCooldown;
        this.initialLives = initialLives;
        this.currentLives = initialLives;
        this.screenWidth = screenWidth;
        this.hitInvincibilityTime = hitInvincibilityTime;

        this.cooldownTimer = 0;
        this.shieldTimer = 0;
        this.cooldownBoostTimer = 0;
        this.engineBoostTimer = 0;
        this.devInvincible = false;
        this.hitInvincibilityTimer = 0;
    }


    public boolean update(Input input, double timeScale) {
        move(input, timeScale);
        tickTimers(timeScale);
        return handleShooting(input, timeScale);
    }


    public void move(Input input, double timeScale) {
        boolean left = input.isDown(Keys.A);
        boolean right = input.isDown(Keys.D);

        // Both keys held: no movement
        if (left == right) return;

        double effectiveSpeed = engineBoostTimer > 0 ? baseSpeed * 2.0 : baseSpeed;
        double dx = effectiveSpeed * timeScale;
        double halfWidth = image.getWidth() / 2.0;

        if (left) {
            x = Math.max(halfWidth, x - dx);
        } else {
            x = Math.min(screenWidth - halfWidth, x + dx);
        }
    }

    private void tickTimers(double timeScale) {
        if (cooldownTimer > 0) cooldownTimer -= timeScale;
        if (shieldTimer > 0) shieldTimer -= timeScale;
        if (cooldownBoostTimer > 0) cooldownBoostTimer -= timeScale;
        if (engineBoostTimer > 0) engineBoostTimer -= timeScale;
        if (hitInvincibilityTimer > 0) hitInvincibilityTimer -= timeScale;
    }

    private boolean handleShooting(Input input, double timeScale) {
        if (input.wasPressed(Keys.SPACE) && cooldownTimer <= 0) {
            double effectiveCooldown = cooldownBoostTimer > 0
                    ? Math.max(1, baseCooldown / 3)
                    : baseCooldown;
            cooldownTimer = effectiveCooldown;
            return true;
        }
        return false;
    }

    @Override
    public void draw() {
        image.draw(x, y);
        // Draw shield overlay when invincible via power-up, dev mode, or post-hit grace
        if (isInvincible()) {
            invincibilityImage.draw(x, y);
        }
    }


    public boolean isInvincible() {
        return shieldTimer > 0 || devInvincible || hitInvincibilityTimer > 0;
    }


    public void loseLife() {
        currentLives--;
        hitInvincibilityTimer = hitInvincibilityTime;
    }


    public void gainLife() {
        if (currentLives < initialLives) {
            currentLives++;
        }
    }

    public boolean isDead() {
        return currentLives <= 0;
    }

    public int getLives() {
        return currentLives;
    }


    public void activateShield(int duration) {
        shieldTimer = duration;
    }

    public void activateCooldownBoost(int duration) {
        cooldownBoostTimer = duration;
    }

    public void activateEngineBoost(int duration) {
        engineBoostTimer = duration;
    }


    public void toggleDevInvincible() {
        devInvincible = !devInvincible;
    }

}
