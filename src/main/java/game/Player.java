package game;

import bagel.Image;
import bagel.Input;
import bagel.Keys;

public class Player extends GameObject {

    private int lives;
    private int speed;
    private int shootCooldown;
    private int cooldownTimer;
    private boolean shoot;

    public Player(Image image, double x, double y, int lives, int speed, int shootCooldown) {
        super(image, x, y);
        this.lives = lives;
        this.speed = speed;
        this.shootCooldown = shootCooldown;
        cooldownTimer = 0;
        shoot =  false;
    }

    public boolean update(Input input,  double timeScale) {
        //Shoot flag
        shoot = false;

        //Update movements
        boolean left = input.wasPressed(Keys.A) || input.isDown(Keys.A);
        boolean right = input.wasPressed(Keys.D) || input.isDown(Keys.D);

        //When both keys are held, no movements
        if (left && !right) {
            //move left
            moveLeft(timeScale);
        }else if (!left && right) {
            //move right
            moveRight(timeScale);
        }

        //Update shooting - 仅在单次按下空格时触发（移除isDown判断）
        if (input.wasPressed(Keys.SPACE)) {
            if (canShoot()){
                shoot();
            }
        }

        //Update cooldown
        updateCooldown();

        return shoot;
    }

    private void moveLeft(double timeScale) {
        x -= speed * timeScale;

        //Set boundary
        if (x < 0 + image.getWidth()/2) {
            x = 0 + image.getWidth()/2;
        }
    }

    private void moveRight(double timeScale) {
        x += speed * timeScale;
        //Set boundary
        if (x >= ShadowAliens.screenWidth - image.getWidth()/2) {
            x = ShadowAliens.screenWidth - image.getWidth()/2 - 1;
        }
    }

    public int getLives() {
        return lives;
    }

    private boolean canShoot(){
        return cooldownTimer == 0;
    }

    private void shoot(){
        shoot = true;
        cooldownTimer = shootCooldown;
    }

    private void updateCooldown(){
        if (cooldownTimer > 0)
            cooldownTimer--;
    }

    public void loseLife(){
        lives--;
    }

    public boolean isDead(){
        return lives == 0;
    }
}
