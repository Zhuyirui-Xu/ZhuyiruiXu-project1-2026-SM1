package game;
import bagel.Input;
import java.util.Properties;

/**
 * Base class for different game screens (battle, pause).
 */
public abstract class ScreenState {
    protected Properties gameProps;

    public ScreenState(Properties gameProps) {
        this.gameProps = gameProps;
    }

    public abstract void update(Input input);
    public abstract void draw();
}
