package ru.prokdo.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import ru.prokdo.config.PluginConfig;
import ru.prokdo.config.PluginConfig.Settings;

public class PlayerColorManager {
    private final PluginConfig config;
    private final Predicate<UUID> isAfk;
    private final Map<UUID, PlayerWorld> worlds = new ConcurrentHashMap<>();
    private final Map<UUID, TabName> tabNames = new HashMap<>();

    public record PlayerWorld(String name, Environment environment) {
    }

    private record TabName(Component previous, Component applied) {
    }

    public PlayerColorManager(PluginConfig config, Predicate<UUID> isAfk) {
        this.config = config;
        this.isAfk = isAfk;
    }

    public void update(Player player) {
        final var id = player.getUniqueId();
        final var world = this.readWorld(player);
        this.worlds.put(id, world);

        final var settings = this.config.settings();
        if (!settings.tabEnabled()) {
            this.restoreTabName(player);
            return;
        }

        final var name = Component.text(player.getName(), this.getColor(id, settings, world));
        final var current = player.playerListName();
        if (name.equals(current)) {
            return;
        }

        final var oldName = this.tabNames.get(id);
        final var previous = oldName != null && oldName.applied().equals(current) ? oldName.previous() : current;
        player.playerListName(name);
        this.tabNames.put(id, new TabName(Component.text(player.getName()).equals(previous) ? null : previous, name));
    }

    public void update(Iterable<? extends Player> players) {
        players.forEach(this::update);
    }

    public void remove(Player player) {
        this.worlds.remove(player.getUniqueId());
        this.restoreTabName(player);
    }

    public PlayerWorld getPlayerWorld(Player player) {
        return Bukkit.isPrimaryThread() ? this.readWorld(player) : this.worlds.get(player.getUniqueId());
    }

    public TextColor getColorForPlayer(Player player, Settings settings) {
        return this.getColor(player.getUniqueId(), settings, this.getPlayerWorld(player));
    }

    public TextColor getColorForPlayer(Player player) {
        return this.getColorForPlayer(player, this.config.settings());
    }

    private PlayerWorld readWorld(Player player) {
        final var world = player.getWorld();
        return new PlayerWorld(world.getName(), world.getEnvironment());
    }

    private TextColor getColor(UUID id, Settings settings, PlayerWorld world) {
        if (settings.afkEnabled() && this.isAfk.test(id)) {
            return settings.afkColor();
        }

        return world != null ? settings.colorForWorld(world.name(), world.environment()) : settings.defaultColor();
    }

    private void restoreTabName(Player player) {
        final var tabName = this.tabNames.remove(player.getUniqueId());
        if (tabName != null && tabName.applied().equals(player.playerListName())) {
            player.playerListName(tabName.previous());
        }
    }
}
