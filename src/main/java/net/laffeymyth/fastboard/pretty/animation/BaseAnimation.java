package net.laffeymyth.fastboard.pretty.animation;

import net.laffeymyth.fastboard.pretty.BoardDisplayAnimation;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An abstract base class for scoreboard animations.
 * This class manages the display of a list of components and tracks the current display for each player.
 */
public abstract class BaseAnimation<T> implements BoardDisplayAnimation<T> {
    private final List<T> displays;
    private final Map<Player, Integer> currentDisplayMap = new HashMap<>();

    /**
     * Constructs a new BaseAnimation with the specified list of displays.
     *
     * @param displays the list of displays for the animation
     */
    public BaseAnimation(List<T> displays) {
        this.displays = displays;
    }

    /**
     * Gets the current display for the specified player.
     *
     * @param player the player to get the display for
     * @return the current display for the player
     */
    @Override
    public T getCurrentDisplay(Player player) {
        return displays.get(currentDisplayMap.getOrDefault(player, 0));
    }

    /**
     * Advances to the next display for the specified player.
     *
     * @param player the player to advance the display for
     */
    @Override
    public void nextDisplay(Player player) {
        Integer index = currentDisplayMap.getOrDefault(player, 0);
        currentDisplayMap.put(player, (index + 1) % displays.size());
    }
}
