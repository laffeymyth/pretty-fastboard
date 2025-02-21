package net.laffeymyth.fastboard.pretty;

import fr.mrmicky.fastboard.adventure.FastBoard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;

class BoardListener implements Listener {
    private final Map<Player, FastBoard> playerScoreboards;
    private final Map<Player, BukkitTask> updaterTaskMap;
    private final Map<Player, BukkitTask> animationTaskMap;

    public BoardListener(Map<Player, FastBoard> playerScoreboards, Map<Player, BukkitTask> updaterTaskMap, Map<Player, BukkitTask> animationTaskMap) {
        this.playerScoreboards = playerScoreboards;
        this.updaterTaskMap = updaterTaskMap;
        this.animationTaskMap = animationTaskMap;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onQuit(PlayerQuitEvent event) {
        BukkitTask bukkitTask = updaterTaskMap.remove(event.getPlayer());

        if (!bukkitTask.isCancelled()) {
            bukkitTask.cancel();
        }

        bukkitTask = animationTaskMap.remove(event.getPlayer());

        if (!bukkitTask.isCancelled()) {
            bukkitTask.cancel();
        }

        FastBoard fastBoard = playerScoreboards.remove(event.getPlayer());

        if (fastBoard != null) {
            fastBoard.delete();
        }
    }
}
