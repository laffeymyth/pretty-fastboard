package net.laffeymyth.fastboard.pretty;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.Optional;

class BoardListener<T> implements Listener {
    private final Map<Player, BoardImpl<T>> playerBoards;

    public BoardListener(Map<Player, BoardImpl<T>> playerBoards) {
        this.playerBoards = playerBoards;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        Optional.ofNullable(playerBoards.remove(player))
                .ifPresent(board -> board.remove(player));
    }
}
