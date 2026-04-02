package ru.prokdo.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;

import org.bukkit.Bukkit;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

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
                    config.load();
                    final var onlinePlayers = Bukkit.getOnlinePlayers();
                    colorManager.update(onlinePlayers);
                    afkManager.resetTimer(onlinePlayers);
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }
}
