package ru.prokdo;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import ru.prokdo.listener.DimensionListener;
import ru.prokdo.manager.DimensionManager;

public class DimensionTracker extends JavaPlugin implements Listener {
    private DimensionManager dimensionManager;
    private DimensionListener dimensionListener;

    @Override
    public void onEnable() {
        dimensionManager = new DimensionManager();
        dimensionListener = new DimensionListener(dimensionManager);

        Bukkit.getPluginManager().registerEvents(dimensionListener, this);

        dimensionManager.initializeOnlinePlayers();
    }

    @Override
    public void onDisable() {
        dimensionManager = null;
        dimensionListener = null;
    }
}
