package game;

/**
 * Represents the possible screen transition actions returned by screen update methods.
 */
public enum ScreenAction {
    NONE,
    RESTART_START,
    PAUSE,
    UNPAUSE,
    WIN,
    LOSE,
    RESTART_BATTLE,
}
