package ru.prokdo.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.bukkit.World.Environment;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import ru.prokdo.DimensionTracker;

public class PluginConfig {
    private final DimensionTracker plugin;
    private final ConfigurationSection defaultColors;
    private volatile Settings settings;

    public record Settings(boolean chatEnabled, boolean tabEnabled, boolean afkEnabled, int afkTimeout,
            boolean advancementMessageEnabled, boolean deathMessageEnabled, boolean joinMessageEnabled,
            boolean kickMessageEnabled, boolean quitMessageEnabled, TextColor overworldColor, TextColor netherColor,
            TextColor endColor, TextColor afkColor, TextColor defaultColor, Map<String, TextColor> worldColors) {
        public Settings {
            worldColors = Map.copyOf(worldColors);
        }

        public TextColor colorForWorld(String name, Environment environment) {
            final var worldColor = this.worldColors.get(name);
            if (worldColor != null) {
                return worldColor;
            }

            return switch (environment) {
                case NORMAL -> this.overworldColor;
                case NETHER -> this.netherColor;
                case THE_END -> this.endColor;
                default -> this.defaultColor;
            };
        }
    }

    public PluginConfig(DimensionTracker plugin) {
        this.plugin = plugin;
        this.plugin.saveDefaultConfig();
        try {
            final var defaults = new YamlConfiguration();
            final var resource = Objects.requireNonNull(this.plugin.getResource("config.yml"), "Missing bundled config.yml");
            try (final var reader = new InputStreamReader(resource, StandardCharsets.UTF_8)) {
                defaults.load(reader);
            }
            this.defaultColors = Objects.requireNonNull(defaults.getConfigurationSection("colors"),
                    "Missing colors section in bundled config.yml");
            this.load();
        } catch (IOException | InvalidConfigurationException exception) {
            throw new IllegalStateException("Could not load DimensionTracker config.yml", exception);
        }
    }

    public Settings settings() {
        return this.settings;
    }

    public void load() throws IOException, InvalidConfigurationException {
        final var config = new YamlConfiguration();
        config.load(new File(this.plugin.getDataFolder(), "config.yml"));

        var afkTimeout = config.getInt("afk.timeout", 300);
        if (afkTimeout <= 0) {
            this.plugin.getLogger().warning("AFK timeout must be a positive number, using default (300)");
            afkTimeout = 300;
        }

        final var colors = config.getConfigurationSection("colors");
        this.settings = new Settings(
                config.getBoolean("chat.enabled", true),
                config.getBoolean("tab.enabled", true),
                config.getBoolean("afk.enabled", true),
                afkTimeout,
                config.getBoolean("messages.advancement", true),
                config.getBoolean("messages.death", true),
                config.getBoolean("messages.join", true),
                config.getBoolean("messages.kick", true),
                config.getBoolean("messages.quit", true),
                this.colorWithDefault(colors, "overworld"),
                this.colorWithDefault(colors, "nether"),
                this.colorWithDefault(colors, "end"),
                this.colorWithDefault(colors, "afk"),
                this.colorWithDefault(colors, "default"),
                this.readWorldColors(colors));
    }

    private TextColor colorWithDefault(ConfigurationSection section, String key) {
        final var fallback = Objects.requireNonNull(this.parseColor(this.defaultColors, key, null),
                "Missing or invalid colors." + key + " in bundled config.yml");
        return this.parseColor(section, key, fallback);
    }

    private Map<String, TextColor> readWorldColors(ConfigurationSection colors) {
        final var worldColors = new HashMap<String, TextColor>();
        if (colors == null) {
            return worldColors;
        }

        for (final var key : colors.getKeys(false)) {
            switch (key) {
                case "overworld", "nether", "end", "afk", "default" -> {
                    continue;
                }
                default -> {
                    final var color = this.parseColor(colors, key, null);
                    if (color != null) {
                        worldColors.put(key, color);
                    }
                }
            }
        }
        return worldColors;
    }

    private TextColor parseColor(ConfigurationSection section, String key, TextColor fallback) {
        if (section == null || !section.contains(key)) {
            return fallback;
        }

        final var value = section.getString(key, "").trim();
        final var color = value.startsWith("#")
                ? TextColor.fromHexString(value)
                : NamedTextColor.NAMES.value(value.toLowerCase(Locale.ROOT));
        if (color == null) {
            this.plugin.getLogger().warning(String.format("Invalid color for key %s: %s, using fallback", key, value));
            return fallback;
        }
        return color;
    }
}
