package net.laffeymyth.fastboard.pretty;

import net.kyori.adventure.text.Component;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 * A service interface for managing scoreboards.
 * This interface provides methods to create, register, and unregister scoreboards.
 */
public interface BoardService<T> {

    /**
     * Creates a new scoreboard with animation.
     *
     * @param animation the animation for the board
     * @return the created board
     */
    Board<T> createBoard(BoardDisplayAnimation<T> animation);

    /**
     * Creates a new scoreboard with the specified updater.
     *
     * @param updaters the updaters for the board
     * @return the created board
     */
    Board<T> createBoard(List<BoardUpdater<T>> updaters);

    /**
     * Creates a new scoreboard with the specified updater and animation.
     *
     * @param updaters  the updater fors the board
     * @param animation the animation for the board
     * @return the created board
     */
    Board<T> createBoard(List<BoardUpdater<T>> updaters, BoardDisplayAnimation<T> animation);

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
     * @param updater   the updater fors the board
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
