package game;

import bagel.Image;
import bagel.util.Rectangle;

/**
 * Base class for all game entities that have a position, image, and active state.
 */
public abstract class GameObject {

    protected Image image;
    protected double x;
    protected double y;
    protected boolean active;

    public GameObject(Image image, double x, double y) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.active = true;
    }


    public void draw() {
        if (active) {
            image.draw(x, y);
        }
    }


    public void destroy() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }


    public Rectangle getBoundingBox() {
        return image.getBoundingBoxAt(new bagel.util.Point(x, y));
    }

    public boolean collideWith(GameObject other) {
        return this.getBoundingBox().intersects(other.getBoundingBox());
    }
}
