package ru.prokdo.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.papermc.paper.event.player.AsyncChatEvent;

import ru.prokdo.manager.PlayerColorManager;
import ru.prokdo.config.PluginConfig;

public class PlayerDisplayListener implements Listener {
    private final PluginConfig config;
    private final PlayerColorManager colorManager;

    public PlayerDisplayListener(PluginConfig config, PlayerColorManager colorManager) {
        this.config = config;
        this.colorManager = colorManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final var player = event.getPlayer();
        this.colorManager.update(player);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        final var player = event.getPlayer();
        this.colorManager.update(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        this.colorManager.remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncChatEvent event) {
        final var settings = this.config.settings();
        if (!settings.chatEnabled()) {
            return;
        }

        final var color = this.colorManager.getColorForPlayer(event.getPlayer(), settings);
        final var renderer = event.renderer();
        event.renderer((source, sourceDisplayName, message, viewer) ->
                renderer.render(source, sourceDisplayName.color(color), message, viewer));
    }
}
