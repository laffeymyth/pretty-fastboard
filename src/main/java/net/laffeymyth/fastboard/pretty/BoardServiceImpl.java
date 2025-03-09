package net.laffeymyth.fastboard.pretty;

import fr.mrmicky.fastboard.FastBoardBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

class BoardServiceImpl<T> implements BoardService<T> {
    private final Class<T> boardClass;
    private final Map<Player, BoardImpl<T>> playerBoards = new HashMap<>();
    private final Map<Player, FastBoardBase<T>> playerFastBoards = new HashMap<>();
    private final Map<Player, BoardDisplayAnimation<T>> playerAnimation = new HashMap<>();
    private final Plugin plugin;
    private BoardUpdaterManager<T> boardUpdaterManager;
    private Listener listener;

    public BoardServiceImpl(Class<T> boardClass, Plugin plugin) {
        this.boardClass = boardClass;
        this.plugin = plugin;
    }

    @Override
    public Board<T> createBoard(List<BoardUpdater<T>> boardUpdaters) {
        return new BoardImpl<>(boardClass, playerBoards, playerFastBoards, playerAnimation, boardUpdaterManager, plugin, boardUpdaters, null);
    }

    @Override
    public Board<T> createBoard(List<BoardUpdater<T>> boardUpdaters, BoardDisplayAnimation<T> animation) {
        return new BoardImpl<>(boardClass, playerBoards, playerFastBoards, playerAnimation, boardUpdaterManager, plugin, boardUpdaters, animation);
    }

    @Override
    public Board<T> createBoard(BoardUpdater<T> updater) {
        List<BoardUpdater<T>> boardUpdaters = new ArrayList<>();
        boardUpdaters.add(updater);

        return this.createBoard(boardUpdaters, null);
    }

    @Override
    public Board<T> createBoard(BoardUpdater<T> updater, BoardDisplayAnimation<T> animation) {
        List<BoardUpdater<T>> boardUpdaters = new ArrayList<>();
        boardUpdaters.add(updater);

        return this.createBoard(boardUpdaters, animation);
    }

    public void register() {
        boardUpdaterManager = new BoardUpdaterManager<>(plugin, playerBoards);
        boardUpdaterManager.startUpdateTask();

        listener = new BoardListener<>(playerBoards);
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    public void unregister() {
        boardUpdaterManager.stopUpdateTask();

        playerFastBoards.values().forEach(componentBaseBoard -> {
            if (!componentBaseBoard.isDeleted()) {
                componentBaseBoard.delete();
            }
        });

        playerFastBoards.clear();
        playerBoards.clear();
        playerAnimation.clear();
        HandlerList.unregisterAll(listener);
    }
}
