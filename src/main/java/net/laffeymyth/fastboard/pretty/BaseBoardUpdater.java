package net.laffeymyth.fastboard.pretty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public abstract class BaseBoardUpdater<T> implements BoardUpdater<T> {
    private final long period;
}
