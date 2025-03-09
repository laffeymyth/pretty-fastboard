package net.laffeymyth.fastboard.pretty;

import fr.mrmicky.fastboard.FastBoardBase;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;

@Getter
@Setter
class BoardImpl<T> implements Board<T> {
    private final Class<T> boardClass;
    private final Map<Integer, T> lineMap = new TreeMap<>(Comparator.reverseOrder());
    private final Map<Player, BoardImpl<T>> playerBoards;
    private final Map<Player, FastBoardBase<T>> playerFastBoards;
    private final Map<Player, BoardDisplayAnimation<T>> playerAnimation;
    private final BoardUpdaterManager<T> boardUpdaterManager;
    private final Plugin plugin;
    private List<BoardUpdater<T>> updaters;
    private BoardDisplayAnimation<T> animation;
    private T title;

    public BoardImpl(Class<T> boardClass, Map<Player, BoardImpl<T>> playerBoards, Map<Player, FastBoardBase<T>> playerFastBoards, Map<Player, BoardDisplayAnimation<T>> playerAnimation, BoardUpdaterManager<T> boardUpdaterManager, Plugin plugin, List<BoardUpdater<T>> updaters, BoardDisplayAnimation<T> animation) {
        this.boardClass = boardClass;
        this.playerBoards = playerBoards;
        this.playerFastBoards = playerFastBoards;
        this.playerAnimation = playerAnimation;
        this.boardUpdaterManager = boardUpdaterManager;
        this.plugin = plugin;
        this.updaters = updaters;
        this.animation = animation;
    }

    @Override
    public void remove(Player player) {
        boardUpdaterManager.removeUpdaters(player);
        boardUpdaterManager.removeAnimation(player);

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

        if (updaters != null) {
            boardUpdaterManager.addUpdaters(player, updaters);
        }

        if (animation != null) {
            boardUpdaterManager.addAnimation(player, animation);
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
    public void update(BoardUpdater<T> updater, Player player) {
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
