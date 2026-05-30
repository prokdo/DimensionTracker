package ru.prokdo.manager;

import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import ru.prokdo.config.PluginConfig;

public class PlayerColorManager {
    private final PluginConfig config;
    private AfkManager afkManager;

    public PlayerColorManager(PluginConfig config) {
        this.config = config;
    }

    public void setAfkManager(AfkManager afkManager) {
        this.afkManager = afkManager;
    }

    public void update(Player player) {
        if (!this.config.isTabEnabled()) {
            return;
        }
        final var color = this.getColorForPlayer(player);
        player.playerListName(Component.text(player.getName(), color));
    }

    public void update(Iterable<? extends Player> players) {
        players.forEach(this::update);
    }

    public TextColor getColorForPlayer(Player player) {
        if (this.afkManager != null && this.afkManager.isAfk(player)) {
            return this.config.getAfkColor();
        }

        final var worldName = player.getWorld().getName();
        final var worldColors = this.config.getWorldColors();

        if (worldColors.containsKey(worldName)) {
            return worldColors.get(worldName);
        }

        final var env = player.getWorld().getEnvironment();
        return switch (env) {
            case NORMAL -> this.config.getOverworldColor();
            case NETHER -> this.config.getNetherColor();
            case THE_END -> this.config.getEndColor();
            default -> this.config.getDefaultColor();
        };
    }
}
