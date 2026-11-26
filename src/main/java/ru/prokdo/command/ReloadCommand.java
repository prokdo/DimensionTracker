package ru.prokdo.command;

import java.io.IOException;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import ru.prokdo.config.PluginConfig;
import ru.prokdo.manager.AfkManager;
import ru.prokdo.manager.PlayerColorManager;

public class ReloadCommand {
    private final PluginConfig config;
    private final PlayerColorManager colorManager;
    private final AfkManager afkManager;

    public ReloadCommand(PluginConfig config, PlayerColorManager colorManager, AfkManager afkManager) {
        this.config = config;
        this.colorManager = colorManager;
        this.afkManager = afkManager;
    }

    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("reload")
                .requires(source -> source.getSender().hasPermission("dimensiontracker.reload"))
                .executes(ctx -> {
                    try {
                        this.config.load();
                    } catch (IOException | InvalidConfigurationException exception) {
                        ctx.getSource().getSender().sendMessage(Component.text(
                                "Could not reload config.yml: " + exception.getMessage(), NamedTextColor.RED));
                        return 0;
                    }
                    final var onlinePlayers = Bukkit.getOnlinePlayers();
                    this.afkManager.clear();
                    this.afkManager.recordActivity(onlinePlayers);
                    this.colorManager.update(onlinePlayers);
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }
}
