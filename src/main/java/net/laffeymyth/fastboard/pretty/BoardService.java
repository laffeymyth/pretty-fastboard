package net.laffeymyth.fastboard.pretty;

import net.kyori.adventure.text.Component;
import org.bukkit.plugin.Plugin;

/**
 * A service interface for managing scoreboards.
 * This interface provides methods to create, register, and unregister scoreboards.
 */
public interface BoardService<T> {

    /**
     * Creates a new scoreboard with the specified updater, delay, and period.
     *
     * @param updater the updater for the board
     * @param delay   the initial delay before the first update
     * @param period  the period between updates
     * @return the created board
     */
    Board<T> createBoard(BoardUpdater<T> updater, long delay, long period);

    /**
     * Creates a new scoreboard with the specified updater, delay, period, and animation.
     *
     * @param updater   the updater for the board
     * @param delay     the initial delay before the first update
     * @param period    the period between updates
     * @param animation the animation for the board
     * @return the created board
     */
    Board<T> createBoard(BoardUpdater<T> updater, long delay, long period, BoardDisplayAnimation<T> animation);

    /**
     * Creates a new scoreboard with the specified updater.
     *
     * @param updater the updater for the board
     * @return the created board
     */
    Board<T> createBoard(BoardUpdater<T> updater);

    /**
     * Creates a new scoreboard with the specified updater and animation.
     *
     * @param updater   the updater for the board
     * @param animation the animation for the board
     * @return the created board
     */
    Board<T> createBoard(BoardUpdater<T> updater, BoardDisplayAnimation<T> animation);

    /**
     * Registers the board service.
     */
    void register();

    /**
     * Unregisters the board service.
     */
    void unregister();

    /**
     * Creates a new instance of the BoardService implementation.
     *
     * @param plugin the plugin that owns the board service
     * @return a new instance of the BoardService implementation
     */
    static BoardService<Component> createComponentService(Plugin plugin) {
        return new BoardServiceImpl<>(Component.class, plugin);
    }

    /**
     * Creates a new instance of the BoardService implementation.
     *
     * @param plugin the plugin that owns the board service
     * @return a new instance of the BoardService implementation
     */
    static BoardService<String> createService(Plugin plugin) {
        return new BoardServiceImpl<>(String.class, plugin);
    }
}
