package ru.prokdo.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerQuitEvent.QuitReason;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import ru.prokdo.config.PluginConfig;
import ru.prokdo.manager.PlayerColorManager;

public class SystemMessageListener implements Listener {
    private final PluginConfig config;
    private final PlayerColorManager colorManager;

    public SystemMessageListener(PluginConfig config, PlayerColorManager colorManager) {
        this.config = config;
        this.colorManager = colorManager;
    }

    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        final var settings = this.config.settings();
        if (!settings.advancementMessageEnabled()) {
            return;
        }

        var message = event.message();
        if (message == null) {
            return;
        }

        final var player = event.getPlayer();
        final var color = this.colorManager.getColorForPlayer(player, settings);

        message = this.colorPlayerName(message, player.getName(), color);
        event.message(message);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        final var settings = this.config.settings();
        if (!settings.deathMessageEnabled()) {
            return;
        }

        var message = event.deathMessage();
        if (message == null) {
            return;
        }

        final var player = event.getPlayer();
        final var color = this.colorManager.getColorForPlayer(player, settings);

        message = this.colorPlayerName(message, player.getName(), color);
        event.deathMessage(message);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final var settings = this.config.settings();
        if (!settings.joinMessageEnabled()) {
            return;
        }

        var message = event.joinMessage();
        if (message == null) {
            return;
        }

        final var player = event.getPlayer();
        final var color = this.colorManager.getColorForPlayer(player, settings);

        message = this.colorPlayerName(message, player.getName(), color);
        event.joinMessage(message);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final var settings = this.config.settings();
        final var enabled = event.getReason() == QuitReason.KICKED
                ? settings.kickMessageEnabled()
                : settings.quitMessageEnabled();
        if (!enabled) {
            return;
        }

        var message = event.quitMessage();
        if (message == null) {
            return;
        }

        final var player = event.getPlayer();
        final var color = this.colorManager.getColorForPlayer(player, settings);

        message = this.colorPlayerName(message, player.getName(), color);
        event.quitMessage(message);
    }

    private Component colorPlayerName(Component message, String playerName, TextColor color) {
        return message.replaceText(builder -> builder
                .matchLiteral(playerName)
                .replacement(Component.text(playerName, color)));
    }
}
