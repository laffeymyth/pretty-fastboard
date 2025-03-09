package net.laffeymyth.fastboard.pretty;

import lombok.experimental.UtilityClass;

import java.util.function.Consumer;

@UtilityClass
public class BoardUpdaterUtil {
    public <T> BoardUpdater<T> updater(long period, Consumer<Board<T>> action) {
        return new BaseBoardUpdater<T>(period) {
            @Override
            public void onUpdate(Board<T> board) {
                action.accept(board);
            }
        };
    }
}
