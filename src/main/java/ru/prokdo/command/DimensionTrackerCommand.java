package ru.prokdo.command;

import com.mojang.brigadier.tree.LiteralCommandNode;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

import ru.prokdo.manager.AfkManager;
import ru.prokdo.manager.PlayerColorManager;
import ru.prokdo.config.PluginConfig;

public class DimensionTrackerCommand {
    private final PluginConfig config;
    private final PlayerColorManager colorManager;
    private final AfkManager afkManager;

    public DimensionTrackerCommand(PluginConfig config, PlayerColorManager colorManager, AfkManager afkManager) {
        this.config = config;
        this.colorManager = colorManager;
        this.afkManager = afkManager;
    }

    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("dimensiontracker")
                .then(new ReloadCommand(config, colorManager, afkManager).build())
                .then(new AfkCommand(afkManager).build())
                .build();
    }
}
