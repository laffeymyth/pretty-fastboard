package net.laffeymyth.fastboard.pretty;

import lombok.Getter;

import java.util.List;

@Getter
public class ListDisplayAnimation<T> extends BaseAnimation<T> implements BoardDisplayAnimation<T> {
    private final long period;

    public ListDisplayAnimation(long period, List<T> displays) {
        super(displays);
        this.period = period;
    }
}
