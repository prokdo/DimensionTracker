package ru.prokdo;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;

import ru.prokdo.command.DimensionTrackerCommand;
import ru.prokdo.config.PluginConfig;
import ru.prokdo.integration.papi.DimensionTrackerPlaceholderAPIExpansion;
import ru.prokdo.manager.AfkManager;
import ru.prokdo.manager.PlayerColorManager;
import ru.prokdo.listener.AfkListener;
import ru.prokdo.listener.PlayerDisplayListener;
import ru.prokdo.listener.SystemMessageListener;

public class DimensionTracker extends JavaPlugin {
    private PluginConfig pluginConfig;
    private PlayerColorManager colorManager;
    private PlayerDisplayListener displayListener;
    private AfkManager afkManager;
    private AfkListener afkListener;
    private SystemMessageListener systemMessageListener;

    private DimensionTrackerPlaceholderAPIExpansion pApiExpansion;

    @Override
    public void onEnable() {
        this.pluginConfig = new PluginConfig(this);

        this.colorManager = new PlayerColorManager(this.pluginConfig);
        this.afkManager = new AfkManager(this, this.pluginConfig, this.colorManager);

        this.colorManager.setAfkManager(this.afkManager);

        this.displayListener = new PlayerDisplayListener(this.pluginConfig, this.colorManager);
        this.afkListener = new AfkListener(this.afkManager);
        this.systemMessageListener = new SystemMessageListener(this.pluginConfig, this.colorManager);

        Bukkit.getPluginManager().registerEvents(this.displayListener, this);
        Bukkit.getPluginManager().registerEvents(this.afkListener, this);
        Bukkit.getPluginManager().registerEvents(this.systemMessageListener, this);

        this.registerCommands();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.pApiExpansion = new DimensionTrackerPlaceholderAPIExpansion(this, this.afkManager, this.colorManager);
            this.pApiExpansion.register();
        }

        final var onlinePlayers = Bukkit.getOnlinePlayers();
        this.colorManager.update(onlinePlayers);
        this.afkManager.resetTimer(onlinePlayers);
    }

    private void registerCommands() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final var command = new DimensionTrackerCommand(this.pluginConfig, this.colorManager, this.afkManager);
            event.registrar().register(command.build(), "DimensionTracker plugin command", List.of("dt"));
        });
    }
}
