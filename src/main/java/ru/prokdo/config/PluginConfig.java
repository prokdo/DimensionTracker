package ru.prokdo.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import ru.prokdo.DimensionTracker;

public class PluginConfig {
    private final DimensionTracker plugin;

    private boolean chatEnabled;
    private boolean tabEnabled;

    private boolean afkEnabled;

    private boolean advancementMessageEnabled;
    private boolean deathMessageEnabled;
    private boolean joinMessageEnabled;
    private boolean kickMessageEnabled;
    private boolean quitMessageEnabled;

    private int afkTimeout;
    private TextColor afkColor;

    private TextColor overworldColor;
    private TextColor netherColor;
    private TextColor endColor;
    private TextColor defaultColor;

    private final Map<String, TextColor> worldColors = new HashMap<>();

    public PluginConfig(DimensionTracker plugin) {
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

    public boolean isAfkEnabled() {
        return afkEnabled;
    }

    public boolean isAdvancementMessageEnabled() {
        return advancementMessageEnabled;
    }

    public boolean isDeathMessageEnabled() {
        return deathMessageEnabled;
    }

    public boolean isKickMessageEnabled() {
        return kickMessageEnabled;
    }

    public boolean isJoinMessageEnabled() {
        return joinMessageEnabled;
    }

    public boolean isQuitMessageEnabled() {
        return quitMessageEnabled;
    }

    public int getAfkTimeout() {
        return afkTimeout;
    }

    public TextColor getAfkColor() {
        return afkColor;
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

        afkEnabled = config.getBoolean("afk.enabled", true);

        joinMessageEnabled = config.getBoolean("messages.join", true);
        quitMessageEnabled = config.getBoolean("messages.quit", true);
        deathMessageEnabled = config.getBoolean("messages.death", true);
        advancementMessageEnabled = config.getBoolean("messages.advancement", true);

        afkTimeout = config.getInt("afk.timeout", 300);
        if (afkTimeout <= 0) {
            plugin.getLogger().warning("AFK timeout must be a positive number, using default (300)");
            afkTimeout = 300;
        }

        final var colors = config.getConfigurationSection("colors");

        overworldColor = parseColor(colors, "overworld", NamedTextColor.GREEN);
        netherColor = parseColor(colors, "nether", NamedTextColor.RED);
        endColor = parseColor(colors, "end", NamedTextColor.LIGHT_PURPLE);
        afkColor = parseColor(colors, "afk", NamedTextColor.GRAY);
        defaultColor = parseColor(colors, "default", NamedTextColor.WHITE);

        registerWorldColors(colors);
    }

    private void registerWorldColors(ConfigurationSection colors) {
        worldColors.clear();
        if (colors != null) {
            for (final var key : colors.getKeys(false)) {
                if (key.equals("overworld") || key.equals("nether") ||
                        key.equals("end") || key.equals("afk") ||
                        key.equals("default")) {
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
