package net.laffeymyth.fastboard.pretty.animation;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public interface BoardDisplayAnimation {

    Component getCurrentDisplay(Player player);

    void nextDisplay(Player player);

    long period();

    long delay();
}