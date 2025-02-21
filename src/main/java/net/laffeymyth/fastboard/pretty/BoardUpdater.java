package net.laffeymyth.fastboard.pretty;

@FunctionalInterface
public interface BoardUpdater {

    void onUpdate(Board board);
}
