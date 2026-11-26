# DimensionTracker

[![CurseForge](https://img.shields.io/badge/Доступно%20на-CurseForge-F16436?logo=curseforge&style=flat)](https://curseforge.com/minecraft/bukkit-plugins/dimension-tracker) [![Modrinth](https://img.shields.io/badge/Доступно%20на-Modrinth-00AF5C?logo=modrinth&style=flat)](https://modrinth.com/plugin/dimensiontracker) [![Сборка](https://github.com/prokdo/DimensionTracker/actions/workflows/ci.yml/badge.svg?branch=main&event=push)](https://github.com/prokdo/DimensionTracker/actions/workflows/ci.yml?query=branch%3Amain+event%3Apush)

---

[[🇬🇧 EN]](README.md) | [[🇷🇺 RU]](README_RU.md)

Плагин для Minecraft серверов, основанных на Paper, который окрашивает имена игроков в чате и TAB-списке в зависимости от текущего измерения

## Возможности

- Цвет имени в чате в зависимости от измерения
- Цвет имени в TAB-списке в зависимости от измерения
- Полная настройка цветов для каждого измерения и мира (поддерживаются HEX и именованные цвета)
- Поддержка кастомных миров
- Возможность независимо отключить окрашивание в чате или TAB
- Встроенное определение AFK с настраиваемым таймаутом и цветом
- Ручное переключение AFK
- Не требует начальной настройки, работает из коробки
- Опциональная интеграция с [PlaceholderAPI](https://github.com/PlaceholderAPI/PlaceholderAPI)

## Цвета

| Измерение               | Цвет       |
| ----------------------- | ---------- |
| Верхний мир (Overworld) | Зелёный    |
| Нижний мир (Nether)     | Красный    |
| Край (The End)          | Фиолетовый |
| AFK                     | Серый      |

## Требования

| Целевая версия Paper | Java для сервера |
| -------------------- | ---------------- |
| 1.21.x               | 21               |
| 26.x (с 26.1)        | 25               |

Для обеих веток используется один JAR: он собирается под API Paper 1.21 и байткод Java 21.

## Установка

1. Скачать актуальный `.jar` файл со страницы [Releases](https://github.com/prokdo/DimensionTracker/releases)
2. Поместить файл в папку `plugins` сервера
3. Перезапустить сервер

## Сборка из исходников

Установите JDK 21 и используйте Gradle Wrapper из репозитория. Если по умолчанию выбрана другая версия Java, укажите путь к JDK 21 в `JAVA_HOME`. На Windows используйте `.\gradlew.bat` вместо `./gradlew`.

```sh
./gradlew build
```

Результат: `build/libs/DimensionTracker-<version>.jar`

`build.gradle.kts` выбирает компилятор из JDK 21 и подставляет версию плагина и `api-version` в `plugin.yml`. Paper API и PlaceholderAPI нужны только для компиляции и не включаются в JAR. Сборка использует опубликованный API Paper `1.21-R0.1-SNAPSHOT`.

Для pull request и push в `main` GitHub Actions использует версию `<12 символов SHA коммита>-dev`. Тег формата `vMAJOR.MINOR.PATCH` задаёт версию в имени JAR и в `plugin.yml`: из `v1.4.1` получится `DimensionTracker-1.4.1.jar`, а из `v1.5.0` — `DimensionTracker-1.5.0.jar`. Версия обычной локальной сборки — `local-dev`. Чтобы локально собрать версию из тега, запустите `./gradlew clean build -PreleaseTag=v1.4.1`.

Workflow сохраняет JAR как артефакт `DimensionTracker`. Релиз на GitHub публикуется отдельно.

## Wiki

Есть вопросы? Смотрите [wiki](https://github.com/prokdo/DimensionTracker/wiki) для подробной информации и документации

## Лицензирование

MIT License — свободное использование, включая коммерческие сервера
