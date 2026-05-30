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
        this.plugin.saveDefaultConfig();
        this.load();
    }

    public boolean isChatEnabled() {
        return this.chatEnabled;
    }

    public boolean isTabEnabled() {
        return this.tabEnabled;
    }

    public boolean isAfkEnabled() {
        return this.afkEnabled;
    }

    public boolean isAdvancementMessageEnabled() {
        return this.advancementMessageEnabled;
    }

    public boolean isDeathMessageEnabled() {
        return this.deathMessageEnabled;
    }

    public boolean isKickMessageEnabled() {
        return this.kickMessageEnabled;
    }

    public boolean isJoinMessageEnabled() {
        return this.joinMessageEnabled;
    }

    public boolean isQuitMessageEnabled() {
        return this.quitMessageEnabled;
    }

    public int getAfkTimeout() {
        return this.afkTimeout;
    }

    public TextColor getAfkColor() {
        return this.afkColor;
    }

    public TextColor getOverworldColor() {
        return this.overworldColor;
    }

    public TextColor getNetherColor() {
        return this.netherColor;
    }

    public TextColor getEndColor() {
        return this.endColor;
    }

    public TextColor getDefaultColor() {
        return this.defaultColor;
    }

    public Map<String, TextColor> getWorldColors() {
        return Collections.unmodifiableMap(this.worldColors);
    }

    public void load() {
        this.plugin.reloadConfig();
        final var config = this.plugin.getConfig();

        this.chatEnabled = config.getBoolean("chat.enabled", true);
        this.tabEnabled = config.getBoolean("tab.enabled", true);

        this.afkEnabled = config.getBoolean("afk.enabled", true);

        this.joinMessageEnabled = config.getBoolean("messages.join", true);
        this.quitMessageEnabled = config.getBoolean("messages.quit", true);
        this.deathMessageEnabled = config.getBoolean("messages.death", true);
        this.advancementMessageEnabled = config.getBoolean("messages.advancement", true);

        this.afkTimeout = config.getInt("afk.timeout", 300);
        if (this.afkTimeout <= 0) {
            plugin.getLogger().warning("AFK timeout must be a positive number, using default (300)");
            this.afkTimeout = 300;
        }

        final var colors = config.getConfigurationSection("colors");

        this.overworldColor = parseColor(colors, "overworld", NamedTextColor.GREEN);
        this.netherColor = parseColor(colors, "nether", NamedTextColor.RED);
        this.endColor = parseColor(colors, "end", NamedTextColor.LIGHT_PURPLE);
        this.afkColor = parseColor(colors, "afk", NamedTextColor.GRAY);
        this.defaultColor = parseColor(colors, "default", NamedTextColor.WHITE);

        this.registerWorldColors(colors);
    }

    private void registerWorldColors(ConfigurationSection colors) {
        this.worldColors.clear();
        if (colors != null) {
            for (final var key : colors.getKeys(false)) {
                if (key.equals("overworld") || key.equals("nether") ||
                        key.equals("end") || key.equals("afk") ||
                        key.equals("default")) {
                    continue;
                }
                final var color = this.parseColor(colors, key, null);
                if (color != null) {
                    this.worldColors.put(key, color);
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
            this.plugin.getLogger()
                    .warning(String.format("Invalid HEX color for key %s: %s, using fallback", key, value));
            return fallback;
        }

        final var named = NamedTextColor.NAMES.value(value.toLowerCase());
        if (named != null) {
            return named;
        }

        this.plugin.getLogger().warning(String.format("Unknown color for key %s: %s, using fallback", key, value));
        return fallback;
    }
}
