package net.laffeymyth.fastboard.pretty;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class BoardUpdaterManager<T> {
    private final BukkitScheduler scheduler = Bukkit.getScheduler();
    private final Map<Player, Map<BoardUpdater<T>, Long>> boardUpdaterPeriods = new HashMap<>();
    private final Map<Player, Map<BoardDisplayAnimation<T>, Long>> animationPeriods = new HashMap<>();
    private final Map<Player, BoardImpl<T>> playerBoards;
    private final Plugin plugin;
    private BukkitTask updateTask;

    public BoardUpdaterManager(Plugin plugin, Map<Player, BoardImpl<T>> playerBoards) {
        this.plugin = plugin;
        this.playerBoards = playerBoards;
    }

    public void addUpdaters(Player player, List<BoardUpdater<T>> updaters) {
        Map<BoardUpdater<T>, Long> periods = boardUpdaterPeriods.computeIfAbsent(player, k -> new HashMap<>());
        updaters.forEach(updater -> periods.put(updater, updater.getPeriod()));
    }

    public void addAnimation(Player player, BoardDisplayAnimation<T> animation) {
        Map<BoardDisplayAnimation<T>, Long> periods = animationPeriods.computeIfAbsent(player, k -> new HashMap<>());
        periods.put(animation, animation.getPeriod());
    }

    public void removeAnimation(Player player) {
        animationPeriods.remove(player);
    }

    public void removeUpdaters(Player player) {
        boardUpdaterPeriods.remove(player);
    }

    public void startUpdateTask() {
        if (updateTask == null) {
            updateTask = scheduler.runTaskTimer(plugin, this::updateBoards, 0L, 1L);
        }
    }

    public void stopUpdateTask() {
        if (updateTask != null) {
            updateTask.cancel();
            updateTask = null;
        }
    }

    private void updateBoards() {
        updatePeriodicTasks(boardUpdaterPeriods, (updater, board, player) -> board.update(updater, player));
        updatePeriodicTasks(animationPeriods, (animation, board, player) -> board.titleAnimationNext(player));
    }

    private <U extends Periodic> void updatePeriodicTasks(Map<Player, Map<U, Long>> periodsMap, PeriodicTask<U, T> task) {
        for (Map.Entry<Player, Map<U, Long>> entry : periodsMap.entrySet()) {
            Player player = entry.getKey();
            Map<U, Long> periods = entry.getValue();
            BoardImpl<T> board = playerBoards.get(player);

            updatePeriodsForPlayer(periods, board, player, task);
        }
    }

    private <U extends Periodic> void updatePeriodsForPlayer(Map<U, Long> periods, BoardImpl<T> board, Player player, PeriodicTask<U, T> task) {
        for (Map.Entry<U, Long> periodEntry : periods.entrySet()) {
            U periodic = periodEntry.getKey();
            long currentPeriod = periodEntry.getValue();

            if (currentPeriod >= periodic.getPeriod()) {
                executeTaskAndResetPeriod(periods, periodic, board, player, task);
            } else {
                incrementPeriod(periods, periodic, currentPeriod);
            }
        }
    }

    private <U extends Periodic> void executeTaskAndResetPeriod(Map<U, Long> periods, U periodic, BoardImpl<T> board, Player player, PeriodicTask<U, T> task) {
        task.execute(periodic, board, player);
        periods.put(periodic, 0L);
    }

    private <U extends Periodic> void incrementPeriod(Map<U, Long> periods, U periodic, long currentPeriod) {
        periods.put(periodic, currentPeriod + 1L);
    }

    @FunctionalInterface
    private interface PeriodicTask<U extends Periodic, T> {
        void execute(U periodic, BoardImpl<T> board, Player player);
    }

}
