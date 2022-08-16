package com.github.mjaroslav.mjutils.util.logging;

public enum ModLoggerLevel {
    ALL, DEBUG, INFO, WARN, ERROR, OFF, UNKNOWN;

    public static ModLoggerLevel getByName(String name) {
        for (ModLoggerLevel level : values())
            if (level.name().equals(name))
                return level;
        return UNKNOWN;
    }
}
