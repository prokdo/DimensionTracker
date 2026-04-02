package ru.prokdo;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;

import ru.prokdo.command.DimensionTrackerCommand;
import ru.prokdo.config.PluginConfig;
import ru.prokdo.manager.AfkManager;
import ru.prokdo.manager.PlayerColorManager;
import ru.prokdo.listener.AfkListener;
import ru.prokdo.listener.PlayerDisplayListener;

public class DimensionTracker extends JavaPlugin {
    private PluginConfig pluginConfig;
    private PlayerColorManager colorManager;
    private PlayerDisplayListener displayListener;
    private AfkManager afkManager;
    private AfkListener afkListener;

    @Override
    public void onEnable() {
        pluginConfig = new PluginConfig(this);

        colorManager = new PlayerColorManager(pluginConfig);
        afkManager = new AfkManager(this, pluginConfig, colorManager);

        colorManager.setAfkManager(afkManager);

        displayListener = new PlayerDisplayListener(pluginConfig, colorManager);
        afkListener = new AfkListener(afkManager);

        Bukkit.getPluginManager().registerEvents(displayListener, this);
        Bukkit.getPluginManager().registerEvents(afkListener, this);

        registerCommands();

        final var onlinePlayers = Bukkit.getOnlinePlayers();
        colorManager.update(onlinePlayers);
        afkManager.resetTimer(onlinePlayers);
    }

    private void registerCommands() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final var command = new DimensionTrackerCommand(pluginConfig, colorManager, afkManager);
            event.registrar().register(command.build(), "DimensionTracker plugin command", List.of("dt"));
        });
    }
}
