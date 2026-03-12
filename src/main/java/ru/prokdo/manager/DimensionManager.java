package ru.prokdo.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class DimensionManager {
    private final Map<UUID, World.Environment> playerDimensions = new HashMap<>();

    public void update(Player player) {
        playerDimensions.put(player.getUniqueId(), player.getWorld().getEnvironment());
        updatePlayerTabList(player);
    }

    public void remove(Player player) {
        playerDimensions.remove(player.getUniqueId());
        updatePlayerTabList(player);
    }

    public World.Environment get(Player player) {
        return playerDimensions.getOrDefault(player.getUniqueId(), World.Environment.NORMAL);
    }

    public void initializeOnlinePlayers() {
        for (final var player : Bukkit.getOnlinePlayers()) {
            update(player);
        }
    }

    public NamedTextColor getColorForPlayer(Player player) {
        final var env = get(player);
        return getColorForEnvironment(env);
    }

    private void updatePlayerTabList(Player player) {
        final var env = get(player);
        final var color = getColorForEnvironment(env);
        player.playerListName(Component.text(player.getName(), color));
    }

    private NamedTextColor getColorForEnvironment(World.Environment env) {
        return switch (env) {
            case NORMAL -> NamedTextColor.GREEN;
            case NETHER -> NamedTextColor.RED;
            case THE_END -> NamedTextColor.LIGHT_PURPLE;
            default -> NamedTextColor.WHITE;
        };
    }
}
