package net.laffeymyth.fastboard.pretty;

import fr.mrmicky.fastboard.FastBoardBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

class BoardServiceImpl<T> implements BoardService<T> {
    private final Class<T> boardClass;
    private final Map<Player, BoardImpl<T>> playerBoards = new HashMap<>();
    private final Map<Player, FastBoardBase<T>> playerFastBoards = new HashMap<>();
    private final Map<Player, BukkitTask> updaterTaskMap = new HashMap<>();
    private final Map<Player, BukkitTask> animationTaskMap = new HashMap<>();
    private final Map<Player, BoardDisplayAnimation<T>> playerAnimation = new HashMap<>();
    private final Plugin plugin;
    private Listener listener;

    public BoardServiceImpl(Class<T> boardClass, Plugin plugin) {
        this.boardClass = boardClass;
        this.plugin = plugin;
    }

    @Override
    public Board<T> createBoard(BoardUpdater<T> updater, long delay, long period) {
        return new BoardImpl<>(boardClass, playerBoards, playerFastBoards, playerAnimation, updaterTaskMap, animationTaskMap, plugin, delay, period, updater, null);
    }

    @Override
    public Board<T> createBoard(BoardUpdater<T> updater, long delay, long period, BoardDisplayAnimation<T> animation) {
        return new BoardImpl<>(boardClass, playerBoards, playerFastBoards, playerAnimation, updaterTaskMap, animationTaskMap, plugin, delay, period, updater, animation);
    }

    @Override
    public Board<T> createBoard(BoardUpdater<T> updater) {
        return new BoardImpl<>(boardClass, playerBoards, playerFastBoards, playerAnimation, updaterTaskMap, animationTaskMap, plugin, updater, null);
    }

    @Override
    public Board<T> createBoard(BoardUpdater<T> updater, BoardDisplayAnimation<T> animation) {
        return new BoardImpl<>(boardClass, playerBoards, playerFastBoards, playerAnimation, updaterTaskMap, animationTaskMap, plugin, updater, animation);
    }

    public void register() {
        listener = new BoardListener<>(playerBoards);
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    public void unregister() {
        updaterTaskMap.values().forEach(bukkitTask -> {
            if (!bukkitTask.isCancelled()) {
                bukkitTask.cancel();
            }
        });

        animationTaskMap.values().forEach(bukkitTask -> {
            if (!bukkitTask.isCancelled()) {
                bukkitTask.cancel();
            }
        });

        playerFastBoards.values().forEach(componentBaseBoard -> {
            if (!componentBaseBoard.isDeleted()) {
                componentBaseBoard.delete();
            }
        });

        playerFastBoards.clear();
        playerBoards.clear();
        updaterTaskMap.clear();
        animationTaskMap.clear();
        playerAnimation.clear();
        HandlerList.unregisterAll(listener);
    }
}
