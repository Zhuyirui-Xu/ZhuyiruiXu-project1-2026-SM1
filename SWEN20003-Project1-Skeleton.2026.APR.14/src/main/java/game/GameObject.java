package game;

import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

// Base class for all visible game things
public abstract class GameObject {
    protected final Image image;
    protected double x;
    protected double y;
    protected boolean active;

    public GameObject(Image image, double x, double y) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.active = true;
    }

    public void draw(){
        image.draw(x, y);
    }

    // For collision detection
    public Rectangle getBoundingBox(){
        return image.getBoundingBoxAt(new Point(x, y));
    }

    // Check if two objects hit each other
    public boolean collideWith(GameObject other){
        return getBoundingBox().intersects(other.getBoundingBox());
    }

    public boolean isActive() {
        return active;
    }

    // Mark object to be removed
    public void destroy(){
        active = false;
    }
}
