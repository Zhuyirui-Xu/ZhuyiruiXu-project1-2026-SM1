package game;

import bagel.Image;

public class Explosion extends GameObject {
    private int duration;
    private double countDown;

    public Explosion(Image image, double x, double y, int duration) {
        super(image, x, y);
        this.duration = duration;
        countDown = this.duration;
    }

    public void update(double timeScale){

        countDown -= timeScale;
        if(countDown <= 0){
            destroy();
        }
    }
}
