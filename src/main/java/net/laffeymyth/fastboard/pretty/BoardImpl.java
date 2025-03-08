package net.laffeymyth.fastboard.pretty;

import fr.mrmicky.fastboard.FastBoardBase;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@Getter
@Setter
class BoardImpl<T> implements Board<T> {
    private static final BukkitScheduler SCHEDULER = Bukkit.getScheduler();
    private final Class<T> boardClass;
    private final Map<Integer, T> lineMap = new TreeMap<>(Comparator.reverseOrder());
    private final Map<Player, BoardImpl<T>> playerBoards;
    private final Map<Player, FastBoardBase<T>> playerFastBoards;
    private final Map<Player, BoardDisplayAnimation<T>> playerAnimation;
    private final Map<Player, BukkitTask> updaterTaskMap;
    private final Map<Player, BukkitTask> animationTaskMap;
    private final Plugin plugin;
    private final long delay;
    private final long period;
    private BoardUpdater<T> updater;
    private BoardDisplayAnimation<T> animation;
    private T title;

    public BoardImpl(Class<T> boardClass, Map<Player, BoardImpl<T>> playerBoards, Map<Player, FastBoardBase<T>> playerFastBoards, Map<Player, BoardDisplayAnimation<T>> playerAnimation, Map<Player, BukkitTask> updaterTaskMap, Map<Player, BukkitTask> animationTaskMap, Plugin plugin, long delay, long period, BoardUpdater<T> updater, BoardDisplayAnimation<T> animation) {
        this.boardClass = boardClass;
        this.playerBoards = playerBoards;
        this.playerFastBoards = playerFastBoards;
        this.playerAnimation = playerAnimation;
        this.updaterTaskMap = updaterTaskMap;
        this.animationTaskMap = animationTaskMap;
        this.plugin = plugin;
        this.delay = delay;
        this.period = period;
        this.updater = updater;
        this.animation = animation;
    }

    public BoardImpl(Class<T> boardClass, Map<Player, BoardImpl<T>> playerBoards, Map<Player, FastBoardBase<T>> playerFastBoards, Map<Player, BoardDisplayAnimation<T>> playerAnimation, Map<Player, BukkitTask> updaterTaskMap, Map<Player, BukkitTask> animationTaskMap, Plugin plugin, BoardUpdater<T> updater, BoardDisplayAnimation<T> animation) {
        this.boardClass = boardClass;
        this.playerBoards = playerBoards;
        this.playerFastBoards = playerFastBoards;
        this.playerAnimation = playerAnimation;
        this.updaterTaskMap = updaterTaskMap;
        this.animationTaskMap = animationTaskMap;
        this.plugin = plugin;
        this.delay = -1L;
        this.period = -1L;
        this.updater = updater;
        this.animation = animation;
    }

    @Override
    public void remove(Player player) {
        Optional.ofNullable(updaterTaskMap.remove(player))
                .filter(task -> !task.isCancelled())
                .ifPresent(BukkitTask::cancel);

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

        if (updater == null) {
            return;
        }

        remove(player);

        playerFastBoards.put(player, BoardFactory.createFastBoardBase(player, boardClass));
        playerBoards.put(player, this);

        if (delay >= 0 && period >= 0) {
            updaterTaskMap.computeIfAbsent(player, player1 -> SCHEDULER.runTaskTimer(plugin, () -> update(player), delay, period));
        }

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

        updater.onUpdate(this);

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
