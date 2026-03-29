# DimensionTracker

[![CurseForge](https://img.shields.io/badge/Availabe%20on-CurseForge-F16436?logo=curseforge&style=flat)](https://curseforge.com/minecraft/bukkit-plugins/dimension-tracker) [![Modrinth](https://img.shields.io/badge/Availabe%20on-Modrinth-00AF5C?logo=modrinth&style=flat)](https://modrinth.com/plugin/dimensiontracker)

---

[🇬🇧 EN](README.md) | [🇷🇺 RU](README_RU.md)

Plugin for Minecraft Paper-based servers that colors player names in chat and TAB list based on current dimension

## Features

- Chat name colors by dimension
- TAB list name colors by dimension
- Fully configurable colors per dimension and per world (HEX and named colors supported)
- Custom worlds support
- Ability to disable chat or TAB coloring independently
- No initial configuration required, works out of the box

## Colors

| Dimension | Color  |
| --------- | ------ |
| Overworld | Green  |
| Nether    | Red    |
| The End   | Purple |

## Installation

1. Download the latest for your version `.jar` file from the [Releases](https://github.com/prokdo/DimensionTracker/releases) page
2. Place the file in your server's `plugins` directory
3. Restart your server

## Configuration

The config file is located at `plugins/DimensionTracker/config.yml` and is created automatically on first launch with the following contents:

```yaml
chat:
  enabled: true

tab:
  enabled: true

colors:
  overworld: "#00FF00" # or GREEN / green
  nether: "#FF0000" # or RED / red
  end: "#FF55FF" # or LIGHT_PURPLE / light_purple
  default: "#FFFFFF" # or WHITE / white


  # Custom world example:
  # custom_world_name: "#RRGGBB" or standard color name
```

> **Note:** This plugin supports configuration update "on the fly" via `/dimensiontracker reload` or `/dt reload` — a full server restart is not needed

Colors can be specified as HEX values (`#RRGGBB`) or named colors (`green`, `red`, `white`, etc.)

For custom worlds, add an entry with the world's folder name as the key. Custom world names take priority over dimension-based colors. Any world not listed falls back to `default`

## Commands

| Command                                    | Description       | Permission                | Default permission level |
| ------------------------------------------ | ----------------- | ------------------------- | ------------------------ |
| `/dimensiontracker reload` or `/dt reload` | Reload the config | `dimensiontracker.reload` | op                       |

## Requirements

- Minecraft version: 1.21+
- Server core: Paper
- Java version: 21+

## Building from Source

> **Note:** JDK 21 or higher and Maven required

```bash
# Clone the repository
git clone https://github.com/prokdo/DimensionTracker.git
cd DimensionTracker

# Build with Maven
mvn clean package

# The built jar will be in target/DimensionTracker-<version>.jar
```

## Troubleshooting

| Problem                                | Solution                                                                                                                               |
| -------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------- |
| Plugin doesn't load                    | Check server console for errors on startup                                                                                             |
| Names aren't colored in chat or in TAB | Ensure you're using Paper 1.21+ and no other plugin / datapack overrides display info and corresponding features are enabled in config |
| Config changes don't apply             | Run `/dt reload` or restart the server                                                                                                 |
| Custom world color not working         | Make sure the key matches the world's folder name exactly                                                                              |
| Still having issues?                   | Open an [Issue](https://github.com/prokdo/DimensionTracker/issues)                                                                     |

## License

MIT License — free to use, including commercial servers
