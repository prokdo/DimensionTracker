package ru.prokdo.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;

import org.bukkit.Bukkit;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import ru.prokdo.config.PluginConfig;
import ru.prokdo.manager.DimensionManager;

public class DimensionTrackerCommand {
    private final PluginConfig config;
    private final DimensionManager dimensionManager;

    public DimensionTrackerCommand(PluginConfig config, DimensionManager dimensionManager) {
        this.config = config;
        this.dimensionManager = dimensionManager;
    }

    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("dimensiontracker")
                .then(buildReload())
                .build();
    }

    private LiteralCommandNode<CommandSourceStack> buildReload() {
        return Commands.literal("reload")
                .requires(source -> source.getSender().hasPermission("dimensiontracker.reload"))
                .executes(ctx -> {
                    config.load();
                    Bukkit.getOnlinePlayers().forEach(dimensionManager::update);
                    ctx.getSource().getSender().sendMessage(
                            Component.text("[DimensionTracker] Config reloaded", NamedTextColor.GREEN));
                    return Command.SINGLE_SUCCESS;
                }).build();
    }
}
