package game;

import bagel.Image;

public abstract class GameObject {

    protected final Image image;
    protected double x;
    protected double y;
    protected boolean active;

    public GameObject(Image image, double x, double y, boolean active) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.active = true;
    }

    public void draw(){
        image.draw(x, y);
    }
}
