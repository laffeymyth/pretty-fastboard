package net.laffeymyth.fastboard.pretty;

import net.laffeymyth.fastboard.pretty.animation.BoardDisplayAnimation;
import org.bukkit.plugin.Plugin;

public interface BoardService {

    Board createBoard(BoardUpdater updater, long delay, long period);

    Board createBoard(BoardUpdater updater, long delay, long period, BoardDisplayAnimation animation);

    void register();

    void unregister();

    static BoardService create(Plugin plugin) {
        return new BoardServiceImpl(plugin);
    }
}
