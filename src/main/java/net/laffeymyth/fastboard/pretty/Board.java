package net.laffeymyth.fastboard.pretty;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Represents a scoreboard that can be displayed to players.
 * This interface provides methods to manage the board's content and animation.
 */
public interface Board<T> {

    /**
     * Gets the title of the board.
     *
     * @return the title of the board
     */
    T getTitle();

    /**
     * Sets the title of the board.
     *
     * @param title the new title of the board
     */
    void setTitle(T title);

    /**
     * Gets the map of lines for the board.
     *
     * @return the map of lines for the board
     */
    Map<Integer, T> getLineMap();

    /**
     * Gets the updaters for the board.
     *
     * @return the updaters for the board
     */
    List<BoardUpdater<T>> getUpdaters();

    void setLine(int index, T line);

    void setLineEmpty(int index);

    void removeLine(int index, T line);

    void addUpdater(long period, Consumer<Board<T>> action);

    /**
     * Gets the animation for the board.
     *
     * @return the animation for the board
     */
    BoardDisplayAnimation<T> getAnimation();

    /**
     * Sets the animation for the board.
     *
     * @param animation the new animation for the board
     */
    void setAnimation(BoardDisplayAnimation<T> animation);

    /**
     * Sets the updaters for the board.
     *
     * @param updaters the new updaters for the board
     */
    void setUpdaters(List<BoardUpdater<T>> updaters);

    /**
     * Sends the board to the specified player.
     *
     * @param player the player to receive the board
     */
    void receive(Player player);

    /**
     * Removes the board from the specified player.
     *
     * @param player the player to remove the board from
     */
    void remove(Player player);

    /**
     * Advances the title animation to the next display for the specified player.
     *
     * @param player the player to advance the title animation for
     */
    void titleAnimationNext(Player player);

    /**
     * Updates the board for the specified player.
     *
     * @param player the player to update the board for
     */
    void update(BoardUpdater<T> updater, Player player);
}
