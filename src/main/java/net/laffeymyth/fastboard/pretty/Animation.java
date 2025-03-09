package net.laffeymyth.fastboard.pretty;

import org.bukkit.entity.Player;

public interface Animation<T> {
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

    /**
     * Advances to the next display for the specified player.
     *
     * @param player the player to advance the display for
     */
    T nextDisplayAndGet(Player player);

    /**
     * Advances to the next display for the specified player.
     *
     * @param player the player to advance the display for
     */
    T getAndNextDisplay(Player player);
}
