package ru.prokdo.integration.papi;

import java.util.Locale;

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
            case "afk" -> this.afkManager.isAfk(player.getUniqueId()) ? "afk" : "active";
            case "dimension" -> {
                final var world = this.colorManager.getPlayerWorld(player);
                yield world == null ? "" : switch (world.environment()) {
                    case NORMAL -> "overworld";
                    default -> world.environment().name().toLowerCase(Locale.ROOT);
                };
            }
            case "world" -> {
                final var world = this.colorManager.getPlayerWorld(player);
                yield world != null ? world.name() : "";
            }
            case "color" -> this.colorManager.getColorForPlayer(player).asHexString();
            default -> null;
        };
    }
}
