package net.laffeymyth.fastboard.pretty;

import fr.mrmicky.fastboard.FastBoard;
import fr.mrmicky.fastboard.FastBoardBase;
import org.bukkit.entity.Player;

class BoardFactory {
    @SuppressWarnings("unchecked")
    public static <T> FastBoardBase<T> createFastBoardBase(Player player, Class<T> boardClass) {
        if (boardClass == FastBoard.class) {
            return (FastBoardBase<T>) new FastBoard(player);
        } else if (boardClass == fr.mrmicky.fastboard.adventure.FastBoard.class) {
            return (FastBoardBase<T>) new fr.mrmicky.fastboard.adventure.FastBoard(player);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + boardClass.getName());
        }
    }
}
