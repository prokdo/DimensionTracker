package ru.prokdo.listener;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import ru.prokdo.manager.DimensionManager;

public class DimensionListener implements Listener {
    private final DimensionManager dimensionManager;

    public DimensionListener(DimensionManager dimensionManager) {
        this.dimensionManager = dimensionManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final var player = event.getPlayer();
        dimensionManager.update(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final var player = event.getPlayer();
        dimensionManager.remove(player);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        final var player = event.getPlayer();
        dimensionManager.update(player);
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        final var player = event.getPlayer();
        final var color = dimensionManager.getColorForPlayer(player);

        final var playerName = player.getName();
        final var coloredName = Component.text(playerName, color);
        final var openBracket = Component.text("<", NamedTextColor.WHITE);
        final var closeBracket = Component.text(">", NamedTextColor.WHITE);
        final var space = Component.text(" ");

        event.renderer(ChatRenderer.viewerUnaware((source, sourceDisplayName, message) -> Component.text()
                .append(openBracket)
                .append(coloredName)
                .append(closeBracket)
                .append(space)
                .append(message)
                .build()));

    }
}