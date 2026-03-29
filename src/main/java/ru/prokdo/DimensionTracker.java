package ru.prokdo;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;

import ru.prokdo.command.DimensionTrackerCommand;
import ru.prokdo.config.PluginConfig;
import ru.prokdo.manager.DimensionManager;
import ru.prokdo.listener.DimensionListener;

public class DimensionTracker extends JavaPlugin {
    private PluginConfig pluginConfig;
    private DimensionManager dimensionManager;
    private DimensionListener dimensionListener;

    @Override
    public void onEnable() {
        pluginConfig = new PluginConfig(this);
        dimensionManager = new DimensionManager(pluginConfig);
        dimensionListener = new DimensionListener(pluginConfig, dimensionManager);

        Bukkit.getPluginManager().registerEvents(dimensionListener, this);
        registerCommands();

        dimensionManager.update(Bukkit.getOnlinePlayers());
    }

    private void registerCommands() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final var command = new DimensionTrackerCommand(pluginConfig, dimensionManager);
            event.registrar().register(command.build(), "DimensionTracker plugin command", List.of("dt"));
        });
    }
}
