package game;

import bagel.Image;

public class Enemy extends GameObject {

    private int arrivalTime;
    private int speed;

    public Enemy(Image image, double x, double y, int arrivalTime, int speed) {
        super(image, x, y);
        this.arrivalTime = arrivalTime;
        this.speed = speed;
    }

    public void update(int frameCount){

        //Only update when enemy has arrived
        if(!hasArrived(frameCount)){
            return;
        }
        y += speed;
        if(y >= ShadowAliens.screenHeight + image.getHeight()/2) {
            active = false;
        }
    }



    private boolean hasArrived(int frameCount){
        return frameCount >= arrivalTime ;
    }

}
