package net.laffeymyth.fastboard.pretty;

import net.kyori.adventure.text.Component;
import net.laffeymyth.fastboard.pretty.animation.BoardDisplayAnimation;
import org.bukkit.entity.Player;

import java.util.Map;

public interface Board {

    Component getTitle();

    void setTitle(Component title);

    Map<Integer, Component> getLineMap();

    BoardUpdater getUpdater();

    BoardDisplayAnimation getAnimation();

    void setAnimation(BoardDisplayAnimation animation);

    void setUpdater(BoardUpdater updater);

    void receive(Player player);

    void remove(Player player);
}
