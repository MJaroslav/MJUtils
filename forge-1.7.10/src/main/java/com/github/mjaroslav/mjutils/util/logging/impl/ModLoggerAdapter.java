package com.github.mjaroslav.mjutils.util.logging.impl;

import com.github.mjaroslav.mjutils.util.logging.ModLogger;
import com.github.mjaroslav.mjutils.util.logging.ModLoggerLevel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class ModLoggerAdapter implements ModLogger {
    @Nonnull
    protected final String NAME;

    @Nonnull
    protected ModLoggerLevel level;

    public ModLoggerAdapter(@Nonnull String name) {
        NAME = name;
        level = ModLoggerLevel.INFO;
    }

    @Nonnull
    @Override
    public String getName() {
        return NAME;
    }

    @Nonnull
    @Override
    public ModLoggerLevel getLevel() {
        return level;
    }

    @Override
    public void setLevel(@Nonnull ModLoggerLevel level) {
        this.level = level;
    }

    @Override
    public void log(@Nonnull ModLoggerLevel level, @Nullable String text, @Nullable Object... args) {
        log(level, text, null, args);
    }

    @Override
    public void error(@Nullable String text, @Nullable Object... args) {
        error(text, null, args);
    }

    @Override
    public void error(@Nullable String text, @Nullable Throwable e, @Nullable Object... args) {
        if (ModLoggerLevel.ERROR.compareTo(getLevel()) > -1)
            log(ModLoggerLevel.ERROR, text, e, args);
    }

    @Override
    public void warn(@Nullable String text, @Nullable Object... args) {
        warn(text, null, args);
    }

    @Override
    public void warn(@Nullable String text, @Nullable Throwable e, @Nullable Object... args) {
        if (ModLoggerLevel.WARN.compareTo(getLevel()) > -1)
            log(ModLoggerLevel.WARN, text, e, args);
    }

    @Override
    public void info(@Nullable String text, @Nullable Object... args) {
        info(text, null, args);
    }

    @Override
    public void info(@Nullable String text, @Nullable Throwable e, @Nullable Object... args) {
        if (ModLoggerLevel.INFO.compareTo(getLevel()) > -1)
            log(ModLoggerLevel.INFO, text, e, args);
    }

    @Override
    public void debug(@Nullable String text, @Nullable Object... args) {
        debug(text, null, args);
    }

    @Override
    public void debug(@Nullable String text, @Nullable Throwable e, @Nullable Object... args) {
        if (ModLoggerLevel.DEBUG.compareTo(getLevel()) > -1)
            log(ModLoggerLevel.DEBUG, text, e, args);
    }
}
