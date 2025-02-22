package net.laffeymyth.fastboard.pretty;

import fr.mrmicky.fastboard.adventure.FastBoard;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.laffeymyth.fastboard.pretty.animation.BoardDisplayAnimation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

@Getter
@Setter
class BoardImpl implements Board {
    private static final BukkitScheduler SCHEDULER = Bukkit.getScheduler();
    private final Map<Integer, Component> lineMap = new TreeMap<>(Comparator.reverseOrder());
    private final Map<Player, BoardImpl> playerBoards;
    private final Map<Player, FastBoard> playerFastBoards;
    private final Map<Player, BoardDisplayAnimation> playerAnimation;
    private final Map<Player, BukkitTask> updaterTaskMap;
    private final Map<Player, BukkitTask> animationTaskMap;
    private final Plugin plugin;
    private final long delay;
    private final long period;
    private BoardUpdater updater;
    private BoardDisplayAnimation animation;
    private Component title;

    public BoardImpl(Map<Player, BoardImpl> playerBoards, Map<Player, FastBoard> playerFastBoards, Map<Player, BoardDisplayAnimation> playerAnimation, Map<Player, BukkitTask> updaterTaskMap, Map<Player, BukkitTask> animationTaskMap, Plugin plugin, long delay, long period, BoardUpdater updater, BoardDisplayAnimation animation) {
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

    @Override
    public void remove(Player player) {
        Optional.ofNullable(updaterTaskMap.remove(player))
                .filter(task -> !task.isCancelled())
                .ifPresent(BukkitTask::cancel);

        Optional.ofNullable(animationTaskMap.remove(player))
                .filter(task -> !task.isCancelled())
                .ifPresent(BukkitTask::cancel);

        Optional.ofNullable(playerFastBoards.remove(player))
                .ifPresent(FastBoard::delete);

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

        FastBoard board = playerFastBoards.computeIfAbsent(player, FastBoard::new);

        playerBoards.put(player, this);
        updaterTaskMap.computeIfAbsent(player, player1 -> SCHEDULER.runTaskTimer(plugin, () -> update(player, board), delay, period));
        animationTaskMap.computeIfAbsent(player, player1 -> SCHEDULER.runTaskTimer(plugin, () -> updateAnimation(player, board), animation.getDelay(), animation.getPeriod()));
    }

    private void updateAnimation(Player player, FastBoard fastBoard) {
        if (animation != null) {
            fastBoard.updateTitle(animation.getCurrentDisplay(player));
            animation.getNextDisplay(player);
        }
    }

    private void update(Player player, FastBoard fastBoard) {
        if (!player.isOnline()) {
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
