package ru.prokdo.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;

import org.bukkit.entity.Player;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.CommandSourceStack;

import ru.prokdo.manager.AfkManager;

public class AfkCommand {
    private final AfkManager afkManager;

    public AfkCommand(AfkManager afkManager) {
        this.afkManager = afkManager;
    }

    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("afk")
                .requires(source -> source.getSender() instanceof Player player &&
                        player.hasPermission("dimensiontracker.afk"))
                .executes(ctx -> {
                    final var player = (Player) ctx.getSource().getSender();
                    afkManager.toggle(player);
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }
}
