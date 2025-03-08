# Pretty FastBoard

Pretty FastBoard is an add-on to the popular fastboard library: https://github.com/MrMicky-FR/FastBoard

This library allows you to create dynamic scoreboards very quickly.

## Features

- **Dynamic Updates**: Easily update scoreboard content in real-time using the `BoardUpdater` interface.
- **Animations**: Create animated scoreboards with custom display cycles using the `BoardDisplayAnimation` interface.
- **Player Management**: Send and remove scoreboards from players with simple methods.

## Installation

To use Pretty FastBoard in your project, you need to add the repository and dependency to your `pom.xml` file if you are
using Maven.

### Maven

Add the following repository to your `pom.xml`:

```xml

<repository>
    <id>javaplugg</id>
    <url>https://repo.javaplugg.net/releases/</url>
</repository>
```

Then, add the dependency:

```xml

<dependency>
    <groupId>net.laffeymyth</groupId>
    <artifactId>pretty-fastboard</artifactId>
    <version>1.4.1-SNAPSHOT</version>
</dependency>
```

### Gradle (Groovy)

Add the following repository to your `build.gradle`:

```groovy
repositories {
    maven {
        url 'https://repo.javaplugg.net/releases/'
    }
}
```

Then, add the dependency:

```groovy
dependencies {
    implementation 'net.laffeymyth:pretty-fastboard:1.4.1-SNAPSHOT'
}
```

### Gradle (Kotlin)

Add the following repository to your `build.gradle.kts`:

```kotlin
repositories {
    maven("https://repo.javaplugg.net/releases/")
}
```

Then, add the dependency:

```kotlin
dependencies {
    implementation("net.laffeymyth:pretty-fastboard:1.4.1-SNAPSHOT")
}
```

## Usage

### Creating a Scoreboard

Here is a basic example of how to create and manage a scoreboard using Pretty FastBoard:

```java
package net.laffeymyth.testboard;

import net.laffeymyth.fastboard.pretty.BoardService;
import net.laffeymyth.testboard.board.BoardListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Testboard extends JavaPlugin {
    private net.laffeymyth.fastboard.pretty.BoardService boardService;

    @Override
    public void onEnable() {
        boardService = BoardService.create(this);
        boardService.register();

        Bukkit.getPluginManager().registerEvents(new BoardListener(boardService), this);
    }

    @Override
    public void onDisable() {
        boardService.unregister();
    }
}
```

### Updating the Scoreboard

Implement the `BoardUpdater` interface to define how the scoreboard should be updated:

```java
package net.laffeymyth.testboard.board;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.laffeymyth.fastboard.pretty.BoardService;
net.laffeymyth.fastboard.pretty.component.animation.SimpleAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Arrays;
import java.util.Map;

public class BoardListener implements Listener {
    private final SimpleAnimation simpleAnimation = new SimpleAnimation(0,
            20,
            Arrays.asList(
                    Component.text("Survival", NamedTextColor.RED, TextDecoration.BOLD),
                    Component.text("Survival", NamedTextColor.GOLD, TextDecoration.BOLD),
                    Component.text("Survival", NamedTextColor.WHITE, TextDecoration.BOLD)
            ));

    private final BoardService boardService;

    public BoardListener(BoardService boardService) {
        this.boardService = boardService;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        net.laffeymyth.fastboard.pretty.Board board = boardService.createBoard(board1 -> {
            Map<Integer, Component> lines = board1.getLineMap();
            lines.put(8, Component.text("Online players: ", NamedTextColor.WHITE).append(Component.text(Bukkit.getOnlinePlayers().size(), NamedTextColor.YELLOW)));
            lines.put(7, null);
            lines.put(6, Component.text("Kills: ", NamedTextColor.WHITE).append(Component.text(player.getStatistic(Statistic.PLAYER_KILLS), NamedTextColor.YELLOW)));
            lines.put(5, Component.text("Deaths: ", NamedTextColor.WHITE).append(Component.text(player.getStatistic(Statistic.DEATHS), NamedTextColor.YELLOW)));
            lines.put(4, null);
            lines.put(3, Component.text("Mob Kills: ", NamedTextColor.WHITE).append(Component.text(player.getStatistic(Statistic.MOB_KILLS), NamedTextColor.YELLOW)));
            lines.put(2, null);
            lines.put(1, Component.text("play.example.net", NamedTextColor.YELLOW));
        }, 0L, 20, simpleAnimation);

        board.receive(player);
    }
}
```

## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvements, please open an issue or submit a
pull request.