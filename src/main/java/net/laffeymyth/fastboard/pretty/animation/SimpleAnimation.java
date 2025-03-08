package net.laffeymyth.fastboard.pretty.animation;

import lombok.Getter;

import java.util.List;

/**
 * A simple implementation of a scoreboard animation.
 * This class extends BaseAnimation and adds delay and period properties.
 */
@Getter
public class SimpleAnimation<T> extends BaseAnimation<T> {
    private final long delay;
    private final long period;

    /**
     * Constructs a new SimpleAnimation with the specified delay, period, and list of displays.
     *
     * @param delay    the initial delay before the first update
     * @param period   the period between updates
     * @param displays the list of displays for the animation
     */
    public SimpleAnimation(long delay, long period, List<T> displays) {
        super(displays);
        this.period = period;
        this.delay = delay;
    }
}
