package net.laffeymyth.fastboard.pretty;

import fr.mrmicky.fastboard.adventure.FastBoard;
import net.laffeymyth.fastboard.pretty.animation.BoardDisplayAnimation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

class BoardServiceImpl implements BoardService {
    private final Map<Player, BoardImpl> playerBoards = new HashMap<>();
    private final Map<Player, FastBoard> playerFastBoards = new HashMap<>();
    private final Map<Player, BukkitTask> updaterTaskMap = new HashMap<>();
    private final Map<Player, BukkitTask> animationTaskMap = new HashMap<>();
    private final Map<Player, BoardDisplayAnimation> playerAnimation = new HashMap<>();
    private final Plugin plugin;
    private Listener listener;

    public BoardServiceImpl(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Board createBoard(BoardUpdater updater, long delay, long period) {
        return new BoardImpl(playerBoards, playerFastBoards, playerAnimation, updaterTaskMap, animationTaskMap, plugin, delay, period, updater, null);
    }

    @Override
    public Board createBoard(BoardUpdater updater, long delay, long period, BoardDisplayAnimation animation) {
        return new BoardImpl(playerBoards, playerFastBoards, playerAnimation, updaterTaskMap, animationTaskMap, plugin, delay, period, updater, animation);
    }

    public void register() {
        listener = new BoardListener(playerBoards);
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
        updaterTaskMap.clear();
        animationTaskMap.clear();
        playerAnimation.clear();
        HandlerList.unregisterAll(listener);
    }
}
