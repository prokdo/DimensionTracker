package ru.prokdo.manager;

import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import ru.prokdo.config.PluginConfig;

public class DimensionManager {
    private final PluginConfig config;

    public DimensionManager(PluginConfig config) {
        this.config = config;
    }

    public void update(Player player) {
        if (!config.isTabEnabled()) {
            return;
        }
        final var color = getColorForPlayer(player);
        player.playerListName(Component.text(player.getName(), color));
    }

    public void update(Iterable<? extends Player> players) {
        for (final var player : players) {
            update(player);
        }
    }

    public TextColor getColorForPlayer(Player player) {
        final var worldName = player.getWorld().getName();
        final var worldColors = config.getWorldColors();

        if (worldColors.containsKey(worldName)) {
            return worldColors.get(worldName);
        }

        final var env = player.getWorld().getEnvironment();
        return switch (env) {
            case NORMAL -> config.getOverworldColor();
            case NETHER -> config.getNetherColor();
            case THE_END -> config.getEndColor();
            default -> config.getDefaultColor();
        };
    }
}
