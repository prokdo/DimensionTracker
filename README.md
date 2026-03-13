# DimensionTracker

[![CurseForge](https://img.shields.io/badge/Availabe%20on-CurseForge-F16436?logo=curseforge&style=flat)](https://curseforge.com/minecraft/bukkit-plugins/dimension-tracker) [![Modrinth](https://img.shields.io/badge/Availabe%20on-Modrinth-00AF5C?logo=modrinth&style=flat)](https://modrinth.com/plugin/dimensiontracker)

---

[🇬🇧 EN](README.md) | [🇷🇺 RU](README_RU.md)

Plugin for Minecraft Paper-based servers that colors player names in chat and TAB list based on current dimension

## Features

- Chat name colors by dimension
- TAB list name colors by dimension
- No configuration required, works out of the box

## Colors

| Dimension | Color  |
| --------- | ------ |
| Overworld | Green  |
| Nether    | Red    |
| The End   | Purple |

## Requirements

- Minecraft version: 1.21+
- Server core: Paper
- Java version: 21+

## Installation

1. Download the latest for your version `.jar` file from the [Releases](https://github.com/prokdo/DimensionTracker/releases) page
2. Place the file in your server's `plugins` directory
3. Restart your server

> **Note:** This plugin does not require `/reload` — a full server restart is recommended

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

| Problem                                | Solution                                                                              |
| -------------------------------------- | ------------------------------------------------------------------------------------- |
| Plugin doesn't load                    | Check server console for errors on startup                                            |
| Names aren't colored in chat or in TAB | Ensure you're using Paper 1.21+ and no other plugin / datapack overrides display info |
| Still having issues?                   | Open an [Issue](https://github.com/prokdo/DimensionTracker/issues)                    |

## License

MIT License — free to use, including commercial servers
