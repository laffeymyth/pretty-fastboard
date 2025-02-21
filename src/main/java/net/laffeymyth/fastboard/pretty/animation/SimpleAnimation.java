package net.laffeymyth.fastboard.pretty.animation;

import net.kyori.adventure.text.Component;

import java.util.List;

public class SimpleAnimation extends BaseAnimation {
    private final long delay;
    private final long period;

    public SimpleAnimation(long delay, long period, List<Component> displays) {
        super(displays);
        this.period = period;
        this.delay = delay;
    }

    @Override
    public long period() {
        return period;
    }

    @Override
    public long delay() {
        return delay;
    }
}
