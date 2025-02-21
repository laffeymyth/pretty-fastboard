package net.laffeymyth.fastboard.pretty.animation;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseAnimation implements BoardDisplayAnimation {
    private final List<Component> displays;
    private final Map<Player, Integer> currentDisplayMap = new HashMap<>();

    public BaseAnimation(List<Component> displays) {
        this.displays = displays;
    }

    @Override
    public Component getCurrentDisplay(Player player) {
        return displays.get(currentDisplayMap.getOrDefault(player, 0));
    }

    @Override
    public void nextDisplay(Player player) {
        Integer index = currentDisplayMap.getOrDefault(player, 0);

        currentDisplayMap.put(player, (index + 1) % displays.size());
    }
}