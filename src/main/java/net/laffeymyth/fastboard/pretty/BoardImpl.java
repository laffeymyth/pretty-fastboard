package net.laffeymyth.fastboard.pretty;

import fr.mrmicky.fastboard.FastBoardBase;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

@Getter
@Setter
class BoardImpl<T> implements Board<T> {
    private static final BukkitScheduler SCHEDULER = Bukkit.getScheduler();
    private final Class<T> boardClass;
    private final Map<Integer, T> lineMap = new TreeMap<>(Comparator.reverseOrder());
    private final Map<Player, BoardImpl<T>> playerBoards;
    private final Map<Player, FastBoardBase<T>> playerFastBoards;
    private final Map<Player, BoardDisplayAnimation<T>> playerAnimation;
    private final Map<Player, List<BukkitTask>> updaterTaskMap;
    private final Map<Player, BukkitTask> animationTaskMap;
    private final Plugin plugin;
    private List<BoardUpdater<T>> updaters;
    private BoardDisplayAnimation<T> animation;
    private T title;

    public BoardImpl(Class<T> boardClass, Map<Player, BoardImpl<T>> playerBoards, Map<Player, FastBoardBase<T>> playerFastBoards, Map<Player, BoardDisplayAnimation<T>> playerAnimation, Map<Player, List<BukkitTask>> updaterTaskMap, Map<Player, BukkitTask> animationTaskMap, Plugin plugin, List<BoardUpdater<T>> updaters, BoardDisplayAnimation<T> animation) {
        this.boardClass = boardClass;
        this.playerBoards = playerBoards;
        this.playerFastBoards = playerFastBoards;
        this.playerAnimation = playerAnimation;
        this.updaterTaskMap = updaterTaskMap;
        this.animationTaskMap = animationTaskMap;
        this.plugin = plugin;
        this.updaters = updaters;
        this.animation = animation;
    }

    @Override
    public void remove(Player player) {
        Optional.ofNullable(updaterTaskMap.remove(player))
                .ifPresent(tasks -> tasks.forEach(task -> {
                    if (!task.isCancelled()) {
                        task.cancel();
                    }
                }));

        Optional.ofNullable(animationTaskMap.remove(player))
                .filter(task -> !task.isCancelled())
                .ifPresent(BukkitTask::cancel);

        Optional.ofNullable(playerFastBoards.remove(player))
                .ifPresent(FastBoardBase::delete);

        playerAnimation.remove(player);
        playerBoards.remove(player);
    }

    @Override
    public void receive(Player player) {
        if (player == null) {
            return;
        }

        if (updaters == null || updaters.isEmpty()) {
            return;
        }

        remove(player);

        playerFastBoards.put(player, BoardFactory.createFastBoardBase(player, boardClass));
        playerBoards.put(player, this);

        List<BukkitTask> tasks = new ArrayList<>();
        for (BoardUpdater<T> updater : updaters) {
            if (updater.getDelay() >= 0 && updater.getPeriod() >= 0) {
                tasks.add(SCHEDULER.runTaskTimer(plugin, () -> update(player), updater.getDelay(), updater.getPeriod()));
            }
        }
        updaterTaskMap.put(player, tasks);

        if (animation != null) {
            if (animation.getDelay() >= 0 && animation.getPeriod() >= 0) {
                animationTaskMap.computeIfAbsent(player, player1 -> SCHEDULER.runTaskTimer(plugin, () -> titleAnimationNext(player), animation.getDelay(), animation.getPeriod()));
            }
        }
    }

    @Override
    public void titleAnimationNext(Player player) {
        if (player == null) {
            return;
        }

        if (!player.isOnline()) {
            return;
        }

        FastBoardBase<T> fastBoard = playerFastBoards.get(player);

        if (fastBoard == null) {
            return;
        }

        if (animation != null) {
            fastBoard.updateTitle(animation.getCurrentDisplay(player));
            animation.nextDisplay(player);
        }
    }

    @Override
    public void update(Player player) {
        if (player == null) {
            return;
        }

        if (!player.isOnline()) {
            return;
        }

        FastBoardBase<T> fastBoard = playerFastBoards.get(player);

        if (fastBoard == null) {
            return;
        }

        for (BoardUpdater<T> updater : updaters) {
            updater.onUpdate(this);
        }

        if (animation == null) {
            if (getTitle() != null) {
                fastBoard.updateTitle(getTitle());
            }
        }

        if (getLineMap() != null && !getLineMap().isEmpty()) {
            fastBoard.updateLines(lineMap.values());
        }
    }
}
