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

        public boolean update(Input input) {

            //Shoot flag
            shoot = false;

            //Update movements
            boolean left = input.wasPressed(Keys.A) || input.isDown(Keys.A);//can make input a class static method
            boolean right = input.wasPressed(Keys.D) || input.isDown(Keys.D);

            //When both keys are held, no movements
            if (left && !right) {
                //move left
                moveLeft();
            }else if (!left && right) {
                //move right
                moveRight();
            }

            //Update shooting
            if (input.wasPressed(Keys.SPACE) || input.isDown(Keys.SPACE)) {
                if (canShoot()){
                    shoot();
                }
            }

            //Update cooldown
            updateCooldown();

            return shoot;

        }

    private void moveLeft() {
        x -= speed;

        //Set boundary
        if (x < 0 + image.getWidth()/2) {
            x = 0 + image.getWidth()/2;
        }
    }
    private void moveRight() {
        x += speed;
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
