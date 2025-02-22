package net.laffeymyth.fastboard.pretty;

import net.kyori.adventure.text.Component;
import net.laffeymyth.fastboard.pretty.animation.BoardDisplayAnimation;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Represents a scoreboard that can be displayed to players.
 * This interface provides methods to manage the board's content and animation.
 */
public interface Board {

    /**
     * Gets the title of the board.
     *
     * @return the title of the board
     */
    Component getTitle();

    /**
     * Sets the title of the board.
     *
     * @param title the new title of the board
     */
    void setTitle(Component title);

    /**
     * Gets the map of lines for the board.
     *
     * @return the map of lines for the board
     */
    Map<Integer, Component> getLineMap();

    /**
     * Gets the updater for the board.
     *
     * @return the updater for the board
     */
    BoardUpdater getUpdater();

    /**
     * Gets the animation for the board.
     *
     * @return the animation for the board
     */
    BoardDisplayAnimation getAnimation();

    /**
     * Sets the animation for the board.
     *
     * @param animation the new animation for the board
     */
    void setAnimation(BoardDisplayAnimation animation);

    /**
     * Sets the updater for the board.
     *
     * @param updater the new updater for the board
     */
    void setUpdater(BoardUpdater updater);

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
}
