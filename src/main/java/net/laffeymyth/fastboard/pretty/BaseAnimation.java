package net.laffeymyth.fastboard.pretty;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseAnimation<T> implements Animation<T> {
    private final List<T> displays;
    private final Map<Player, Integer> currentDisplayMap = new HashMap<>();

    public BaseAnimation(List<T> displays) {
        this.displays = displays;
    }

    @Override
    public T getCurrentDisplay(Player player) {
        return displays.get(currentDisplayMap.getOrDefault(player, 0));
    }

    @Override
    public void nextDisplay(Player player) {
        Integer index = currentDisplayMap.getOrDefault(player, 0);
        currentDisplayMap.put(player, (index + 1) % displays.size());
    }

    @Override
    public T nextDisplayAndGet(Player player) {
        nextDisplay(player);
        return getCurrentDisplay(player);
    }

    @Override
    public T getAndNextDisplay(Player player) {
        T currentDisplay = getCurrentDisplay(player);
        nextDisplay(player);
        return currentDisplay;
    }
}
