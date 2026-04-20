package game;

import bagel.Image;

public class Player extends GameObject {

    private int lives;
    private int speed;
    private int shootCooldown;
    private int cooldownTimer;


    public Player(Image image, double x, double y, boolean active) {
        super(image, x, y, active);
    }
}
