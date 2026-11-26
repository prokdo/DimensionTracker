package ru.prokdo.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.bukkit.entity.Player;

import ru.prokdo.config.PluginConfig;
import ru.prokdo.DimensionTracker;

public class AfkManager {
    private final DimensionTracker plugin;
    private final PluginConfig config;
    private final Consumer<Player> onStatusChange;
    private final Set<UUID> afkPlayers;
    private final Map<UUID, Long> lastActivity = new HashMap<>();

    public AfkManager(DimensionTracker plugin, PluginConfig config, Set<UUID> afkPlayers,
            Consumer<Player> onStatusChange) {
        this.plugin = plugin;
        this.config = config;
        this.afkPlayers = afkPlayers;
        this.onStatusChange = onStatusChange;
    }

    public boolean isAfk(UUID id) {
        return this.afkPlayers.contains(id);
    }

    private void setAfk(Player player, boolean afk) {
        final var id = player.getUniqueId();
        final var changed = afk ? this.afkPlayers.add(id) : this.afkPlayers.remove(id);
        if (changed) {
            this.onStatusChange.accept(player);
        }
    }

    public void toggle(Player player) {
        if (!this.config.settings().afkEnabled()) {
            return;
        }
        if (this.isAfk(player.getUniqueId())) {
            this.recordActivity(player);
        } else {
            this.lastActivity.remove(player.getUniqueId());
            this.setAfk(player, true);
        }
    }

    public void recordActivity(Player player) {
        if (!this.config.settings().afkEnabled()) {
            return;
        }

        this.lastActivity.put(player.getUniqueId(), System.nanoTime());
        this.setAfk(player, false);
    }

    public void checkInactivity() {
        final var settings = this.config.settings();
        if (!settings.afkEnabled() || this.lastActivity.isEmpty()) {
            return;
        }

        final var now = System.nanoTime();
        final var timeout = TimeUnit.SECONDS.toNanos(settings.afkTimeout());
        final var server = this.plugin.getServer();
        final var entries = this.lastActivity.entrySet().iterator();
        while (entries.hasNext()) {
            final var entry = entries.next();
            if (now - entry.getValue() >= timeout) {
                final var id = entry.getKey();
                entries.remove();
                final var player = server.getPlayer(id);
                if (player != null && player.isOnline()) {
                    this.setAfk(player, true);
                }
            }
        }
    }

    public void recordChatActivity(Player player, boolean asynchronous) {
        if (!this.config.settings().afkEnabled()) {
            return;
        }
        if (!asynchronous) {
            this.recordActivity(player);
            return;
        }
        final var id = player.getUniqueId();
        final var server = this.plugin.getServer();
        server.getScheduler().runTask(this.plugin, () -> {
            if (server.getPlayer(id) == player && player.isOnline()) {
                this.recordActivity(player);
            }
        });
    }

    public void recordActivity(Iterable<? extends Player> players) {
        players.forEach(this::recordActivity);
    }

    public void remove(Player player) {
        final var id = player.getUniqueId();
        this.afkPlayers.remove(id);
        this.lastActivity.remove(id);
    }

    public void clear() {
        this.lastActivity.clear();
        this.afkPlayers.clear();
    }
}
