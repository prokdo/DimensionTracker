package ru.prokdo.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public class PluginConfig {
    private final JavaPlugin plugin;

    private boolean chatEnabled;
    private boolean tabEnabled;

    private TextColor overworldColor;
    private TextColor netherColor;
    private TextColor endColor;
    private TextColor defaultColor;

    private final Map<String, TextColor> worldColors = new HashMap<>();

    public PluginConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        load();
    }

    public boolean isChatEnabled() {
        return chatEnabled;
    }

    public boolean isTabEnabled() {
        return tabEnabled;
    }

    public TextColor getOverworldColor() {
        return overworldColor;
    }

    public TextColor getNetherColor() {
        return netherColor;
    }

    public TextColor getEndColor() {
        return endColor;
    }

    public TextColor getDefaultColor() {
        return defaultColor;
    }

    public Map<String, TextColor> getWorldColors() {
        return Collections.unmodifiableMap(worldColors);
    }

    public void load() {
        plugin.reloadConfig();
        final var config = plugin.getConfig();

        chatEnabled = config.getBoolean("chat.enabled", true);
        tabEnabled = config.getBoolean("tab.enabled", true);

        final var colors = config.getConfigurationSection("colors");

        overworldColor = parseColor(colors, "overworld", NamedTextColor.GREEN);
        netherColor = parseColor(colors, "nether", NamedTextColor.RED);
        endColor = parseColor(colors, "end", NamedTextColor.LIGHT_PURPLE);
        defaultColor = parseColor(colors, "default", NamedTextColor.WHITE);

        worldColors.clear();
        if (colors != null) {
            for (final var key : colors.getKeys(false)) {
                if (key.equals("overworld") || key.equals("nether") ||
                        key.equals("end") || key.equals("default")) {
                    continue;
                }
                final var color = parseColor(colors, key, null);
                if (color != null) {
                    worldColors.put(key, color);
                }
            }
        }
    }

    private TextColor parseColor(ConfigurationSection section, String key, TextColor fallback) {
        if (section == null || !section.contains(key)) {
            return fallback;
        }

        final var value = section.getString(key, "").trim();

        if (value.startsWith("#")) {
            final var color = TextColor.fromHexString(value);
            if (color != null) {
                return color;
            }
            plugin.getLogger().warning(String.format("Invalid HEX color for key %s: %s, using fallback", key, value));
            return fallback;
        }

        final var named = NamedTextColor.NAMES.value(value.toLowerCase());
        if (named != null) {
            return named;
        }

        plugin.getLogger().warning(String.format("Unknown color for key %s: %s, using fallback", key, value));
        return fallback;
    }
}
