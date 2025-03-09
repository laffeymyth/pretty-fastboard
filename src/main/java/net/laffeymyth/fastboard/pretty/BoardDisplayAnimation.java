package net.laffeymyth.fastboard.pretty;

import org.bukkit.entity.Player;

/**
 * Represents an animation for a scoreboard display.
 * This interface provides methods to manage the animation's display and timing.
 */
public interface BoardDisplayAnimation<T> extends Periodic {

    /**
     * Gets the current display for the specified player.
     *
     * @param player the player to get the display for
     * @return the current display for the player
     */
    T getCurrentDisplay(Player player);

    /**
     * Advances to the next display for the specified player.
     *
     * @param player the player to advance the display for
     */
    void nextDisplay(Player player);
}
