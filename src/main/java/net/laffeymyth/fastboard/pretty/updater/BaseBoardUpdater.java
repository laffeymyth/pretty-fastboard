package net.laffeymyth.fastboard.pretty.updater;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.laffeymyth.fastboard.pretty.BoardUpdater;

@Setter
@Getter
@AllArgsConstructor
public abstract class BaseBoardUpdater<T> implements BoardUpdater<T> {
    private final long period;
    private final long delay;
}
