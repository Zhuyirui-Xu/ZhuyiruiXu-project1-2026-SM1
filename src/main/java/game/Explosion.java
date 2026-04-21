package game;

import bagel.Image;

public class Explosion extends GameObject {

    private int duration;
    private double countDown;


    public Explosion(Image image, double x, double y, int duration) {
        super(image, x, y);
        this.duration = duration;
        countDown = duration;
    }

    public void update(){
        countDown--;
        if(countDown <= 0){
            destroy();
        }
    }
}
