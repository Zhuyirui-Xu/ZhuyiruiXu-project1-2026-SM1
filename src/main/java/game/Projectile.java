package game;

import bagel.Image;

public class Projectile extends GameObject{

    private double speed;

    public Projectile(Image image, double x, double y, double speed) {
        super(image, x, y);
        this.speed = speed;
    }

    public void update() {
        y -= speed;

        if(y < 0 - image.getHeight()/2) {
            destroy();
        }
    }
}
