package game;

import bagel.Input;

import java.awt.*;
import java.util.Properties;

public abstract class Screen {
    //Design choice here:Protected only read no change
    protected Properties gameProps;

    public Screen(Properties gameProps) {
        this.gameProps = gameProps;
    }

    public abstract void update(Input input);
    public abstract void draw();
}
