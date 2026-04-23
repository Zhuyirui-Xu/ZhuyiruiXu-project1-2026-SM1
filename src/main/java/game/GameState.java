package game;
import bagel.Input;
import java.util.Properties;

// All screens must have update and draw
public abstract class GameState {
    protected Properties gameProps;

    public GameState(Properties gameProps) {
        this.gameProps = gameProps;
    }

    public abstract void update(Input input);
    public abstract void draw();
}
