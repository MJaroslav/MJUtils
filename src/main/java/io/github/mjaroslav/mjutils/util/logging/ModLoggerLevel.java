package io.github.mjaroslav.mjutils.util.logging;

import org.jetbrains.annotations.NotNull;

public enum ModLoggerLevel {
    ALL, DEBUG, INFO, WARN, ERROR, OFF, UNKNOWN;

    public static @NotNull ModLoggerLevel getByName(String name) {
        for (var level : values()) if (level.name().equals(name)) return level;
        return UNKNOWN;
    }
}
