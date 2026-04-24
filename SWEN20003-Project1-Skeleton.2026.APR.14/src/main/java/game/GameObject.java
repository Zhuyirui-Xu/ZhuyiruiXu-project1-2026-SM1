package game;

import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Base class for all renderable game entities with shared position and collision logic.
 */
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

    // Provide consistent collision bounds for all game objects
    public Rectangle getBoundingBox(){
        return image.getBoundingBoxAt(new Point(x, y));
    }


    public boolean collideWith(GameObject other){
        return getBoundingBox().intersects(other.getBoundingBox());
    }

    public boolean isActive() {
        return active;
    }

    // Mark object for removal
    public void destroy(){
        active = false;
    }
}
