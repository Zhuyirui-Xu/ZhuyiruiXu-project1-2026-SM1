package game;

import bagel.Image;

/**
 * Enemy unit that spawns at a specific frame and moves vertically down the screen.
 */
public class Enemy extends GameObject {
    private int arrivalTime;
    private int speed;

    public Enemy(Image image, double x, double y, int arrivalTime, int speed) {
        super(image, x, y);
        this.arrivalTime = arrivalTime;
        this.speed = speed;
    }

    public void update(int frameCount, double timeScale){
        // Only start moving once the specified spawn frame is reached
        if(!hasArrived(frameCount)){
            return;
        }

        y += speed * timeScale;

        // Remove object once it has fully left the bottom of the screen
        if(y >= ShadowAliens.screenHeight + image.getHeight()/2) {
            destroy();
        }
    }

    public boolean hasArrived(int frameCount){
        return frameCount >= arrivalTime ;
    }

    public double getX() { return x; }
    public double getY() { return y; }
}
