package net.laffeymyth.fastboard.pretty;

/**
 * A functional interface for updating the content of a scoreboard.
 * Implementations of this interface will define the logic for updating
 * the board's content.
 */
@FunctionalInterface
public interface BoardUpdater {

    /**
     * Called when the board needs to be updated.
     *
     * @param board the board to update
     */
    void onUpdate(Board board);
}
