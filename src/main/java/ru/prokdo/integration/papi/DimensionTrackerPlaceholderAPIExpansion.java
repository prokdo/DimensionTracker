package ru.prokdo.integration.papi;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import ru.prokdo.DimensionTracker;
import ru.prokdo.manager.AfkManager;
import ru.prokdo.manager.PlayerColorManager;

public class DimensionTrackerPlaceholderAPIExpansion extends PlaceholderExpansion {
    private final DimensionTracker plugin;
    private final AfkManager afkManager;
    private final PlayerColorManager colorManager;

    public DimensionTrackerPlaceholderAPIExpansion(DimensionTracker plugin, AfkManager afkManager,
            PlayerColorManager colorManager) {
        this.plugin = plugin;
        this.afkManager = afkManager;
        this.colorManager = colorManager;
    }

    @Override
    public String getIdentifier() {
        return "dimensiontracker";
    }

    @Override
    public String getAuthor() {
        return "prokdo";
    }

    @Override
    public String getVersion() {
        return this.plugin.getPluginMeta().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if (player == null) {
            return "";
        }

        return switch (params) {
            case "afk" -> this.afkManager.isAfk(player) ? "afk" : "active";
            case "dimension" -> player.getWorld().getEnvironment().name().toLowerCase();
            case "world" -> player.getWorld().getName();
            case "color" -> this.colorManager.getColorForPlayer(player).asHexString();
            default -> null;
        };
    }
}
