package ru.prokdo.manager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import ru.prokdo.config.PluginConfig;
import ru.prokdo.DimensionTracker;

public class AfkManager {
    private final DimensionTracker plugin;
    private final PluginConfig config;
    private final PlayerColorManager colorManager;

    private final Map<UUID, Boolean> afkStatuses = new ConcurrentHashMap<>();
    private final Map<UUID, BukkitTask> afkTimers = new ConcurrentHashMap<>();

    public AfkManager(DimensionTracker plugin, PluginConfig config, PlayerColorManager colorManager) {
        this.plugin = plugin;
        this.config = config;
        this.colorManager = colorManager;
    }

    public boolean isAfk(Player player) {
        return this.afkStatuses.getOrDefault(player.getUniqueId(), false);
    }

    public void setAfk(Player player, boolean afk) {
        if (!this.config.isAfkEnabled()) {
            return;
        }
        this.afkStatuses.put(player.getUniqueId(), afk);
        this.colorManager.update(player);
    }

    public void toggle(Player player) {
        if (!this.config.isAfkEnabled()) {
            return;
        }
        this.setAfk(player, !isAfk(player));
    }

    public void resetTimer(Player player) {
        if (!this.config.isAfkEnabled()) {
            return;
        }

        if (this.isAfk(player)) {
            this.setAfk(player, false);
        }

        final var existing = this.afkTimers.remove(player.getUniqueId());
        if (existing != null) {
            existing.cancel();
        }

        final var ticks = this.config.getAfkTimeout() * 20L;
        final var task = this.plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (player.isOnline()) {
                this.setAfk(player, true);
            }
        }, ticks);

        this.afkTimers.put(player.getUniqueId(), task);
    }

    public void resetTimer(Iterable<? extends Player> players) {
        players.forEach(this::resetTimer);
    }

    public void remove(Player player) {
        this.afkStatuses.remove(player.getUniqueId());
        final var task = this.afkTimers.remove(player.getUniqueId());
        if (task != null) {
            task.cancel();
        }
    }
}
