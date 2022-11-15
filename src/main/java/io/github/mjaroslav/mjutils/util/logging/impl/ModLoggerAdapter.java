package io.github.mjaroslav.mjutils.util.logging.impl;

import io.github.mjaroslav.mjutils.util.logging.ModLogger;
import io.github.mjaroslav.mjutils.util.logging.ModLoggerLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@RequiredArgsConstructor
public abstract class ModLoggerAdapter implements ModLogger {
    protected final @NotNull String name;
    @Setter
    protected @NotNull ModLoggerLevel level = ModLoggerLevel.INFO;

    @Override
    public void log(@NotNull ModLoggerLevel level, @Nullable String text, @Nullable Object... args) {
        log(level, text, null, args);
    }

    @Override
    public void error(@Nullable String text, @Nullable Object... args) {
        error(text, null, args);
    }

    @Override
    public void error(@Nullable String text, @Nullable Throwable e, @Nullable Object... args) {
        if (ModLoggerLevel.ERROR.compareTo(getLevel()) > -1) log(ModLoggerLevel.ERROR, text, e, args);
    }

    @Override
    public void warn(@Nullable String text, @Nullable Object... args) {
        warn(text, null, args);
    }

    @Override
    public void warn(@Nullable String text, @Nullable Throwable e, @Nullable Object... args) {
        if (ModLoggerLevel.WARN.compareTo(getLevel()) > -1) log(ModLoggerLevel.WARN, text, e, args);
    }

    @Override
    public void info(@Nullable String text, @Nullable Object... args) {
        info(text, null, args);
    }

    @Override
    public void info(@Nullable String text, @Nullable Throwable e, @Nullable Object... args) {
        if (ModLoggerLevel.INFO.compareTo(getLevel()) > -1) log(ModLoggerLevel.INFO, text, e, args);
    }

    @Override
    public void debug(@Nullable String text, @Nullable Object... args) {
        debug(text, null, args);
    }

    @Override
    public void debug(@Nullable String text, @Nullable Throwable e, @Nullable Object... args) {
        if (ModLoggerLevel.DEBUG.compareTo(getLevel()) > -1) log(ModLoggerLevel.DEBUG, text, e, args);
    }
}
