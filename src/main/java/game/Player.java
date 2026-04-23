package game;

import bagel.Image;
import bagel.Input;
import bagel.Keys;

public class Player extends GameObject {
    private int lives;
    private int moveSpeed;
    private int shootCooldown;
    private int cooldownTimer;
    private boolean isShooting;

    public Player(Image image, double x, double y, int lives, int moveSpeed, int shootCooldown) {
        super(image, x, y);
        this.lives = lives;
        this.moveSpeed = moveSpeed;
        this.shootCooldown = shootCooldown;
        this.cooldownTimer = 0;
        this.isShooting = false;
    }

    public boolean update(Input input, double timeScale) {
        isShooting = false;

        boolean left = input.wasPressed(Keys.A) || input.isDown(Keys.A);
        boolean right = input.wasPressed(Keys.D) || input.isDown(Keys.D);


        if (left && !right) {
            x -= moveSpeed * timeScale;
            if (x < image.getWidth()/2) x = image.getWidth()/2;
        }


        if (right && !left) {
            x += moveSpeed * timeScale;
            double max = ShadowAliens.screenWidth - image.getWidth()/2;
            if (x > max) x = max;
        }

        // Shoot only if cooldown is done
        if (input.wasPressed(Keys.SPACE) && cooldownTimer == 0) {
            isShooting = true;
            cooldownTimer = shootCooldown;
        }

        if (cooldownTimer > 0) cooldownTimer--;
        return isShooting;
    }

    public void loseLife() { lives--; }
    public boolean isDead() { return lives <= 0; }
    public int getLives() { return lives; }
    public double getX() { return x; }
    public double getY() { return y; }
}