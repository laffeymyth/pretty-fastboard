package net.laffeymyth.fastboard.pretty.animation;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

/**
 * Represents an animation for a scoreboard display.
 * This interface provides methods to manage the animation's display and timing.
 */
public interface BoardDisplayAnimation {

    /**
     * Gets the current display for the specified player.
     *
     * @param player the player to get the display for
     * @return the current display for the player
     */
    Component getCurrentDisplay(Player player);

    /**
     * Advances to the next display for the specified player.
     *
     * @param player the player to advance the display for
     */
    void getNextDisplay(Player player);

    /**
     * Gets the period of the animation.
     *
     * @return the period of the animation
     */
    long getPeriod();

    /**
     * Gets the delay of the animation.
     *
     * @return the delay of the animation
     */
    long getDelay();
}
