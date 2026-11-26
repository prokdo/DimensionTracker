# DimensionTracker

[![CurseForge](https://img.shields.io/badge/Availabe%20on-CurseForge-F16436?logo=curseforge&style=flat)](https://curseforge.com/minecraft/bukkit-plugins/dimension-tracker) [![Modrinth](https://img.shields.io/badge/Availabe%20on-Modrinth-00AF5C?logo=modrinth&style=flat)](https://modrinth.com/plugin/dimensiontracker) [![Build](https://github.com/prokdo/DimensionTracker/actions/workflows/ci.yml/badge.svg?branch=main&event=push)](https://github.com/prokdo/DimensionTracker/actions/workflows/ci.yml?query=branch%3Amain+event%3Apush)

---

[[🇬🇧 EN]](README.md) | [[🇷🇺 RU]](README_RU.md)

Plugin for Minecraft Paper-based servers that colors player names in chat and TAB list based on current dimension

## Features

- Chat name colors by dimension
- TAB list name colors by dimension
- Fully configurable colors per dimension and per world (HEX and named colors supported)
- Custom worlds support
- Ability to disable chat or TAB coloring independently
- Built-in AFK detection with configurable timeout and color
- Manual AFK toggle
- No initial configuration required, works out of the box
- Optional [PlaceholderAPI](https://github.com/PlaceholderAPI/PlaceholderAPI) integration

## Colors

| Dimension | Color  |
| --------- | ------ |
| Overworld | Green  |
| Nether    | Red    |
| The End   | Purple |
| AFK       | Gray   |

## Requirements

| Target Paper version | Server Java |
| -------------------- | ----------- |
| 1.21.x               | 21          |
| 26.x (from 26.1)     | 25          |

Use the same JAR for both branches. It targets the Paper 1.21 API and Java 21 bytecode.

## Installation

1. Download the plugin JAR from the [Releases](https://github.com/prokdo/DimensionTracker/releases) page
2. Place the file in your server's `plugins` directory
3. Restart your server

## Building from source

Install JDK 21 and use the repository's Gradle Wrapper. If your default Java is a different version, set `JAVA_HOME` to your JDK 21 installation. On Windows, use `.\gradlew.bat` instead of `./gradlew`.

```sh
./gradlew build
```

Output: `build/libs/DimensionTracker-<version>.jar`

`build.gradle.kts` selects a Java 21 toolchain and fills in the plugin version and `api-version` in `plugin.yml`. Paper API and PlaceholderAPI are compile-only dependencies and aren't bundled in the JAR. The build uses Paper's published `1.21-R0.1-SNAPSHOT` API.

For pull requests and pushes to `main`, GitHub Actions uses `<12-character commit SHA>-dev` as the version. A Git tag in the form `vMAJOR.MINOR.PATCH` supplies the version for both the JAR filename and `plugin.yml`: `v1.4.1` produces `DimensionTracker-1.4.1.jar`, and `v1.5.0` produces `DimensionTracker-1.5.0.jar`. Local builds default to `local-dev`; to build a tagged version locally, run `./gradlew clean build -PreleaseTag=v1.4.1`.

The workflow uploads the JAR as the `DimensionTracker` artifact. GitHub Releases are published separately.

## Wiki

Have a question? See [wiki](https://github.com/prokdo/DimensionTracker/wiki) for detailed information and documentation

## License

MIT License — free to use, including commercial servers
