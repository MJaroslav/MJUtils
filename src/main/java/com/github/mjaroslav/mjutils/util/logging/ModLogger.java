package com.github.mjaroslav.mjutils.util.logging;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ModLogger {
    @Nonnull
    String getName();

    @Nonnull
    ModLoggerLevel getLevel();

    void setLevel(@Nonnull ModLoggerLevel level);

    void log(@Nonnull ModLoggerLevel level, @Nullable String text, @Nullable Object... args);

    void log(@Nonnull ModLoggerLevel level, @Nullable String text, @Nullable Throwable e, @Nullable Object... args);

    void error(@Nullable String text, @Nullable Object... args);

    void error(@Nullable String text, @Nullable Throwable e, @Nullable Object... args);

    void warn(@Nullable String text, @Nullable Object... args);

    void warn(@Nullable String text, @Nullable Throwable e, @Nullable Object... args);

    void info(@Nullable String text, @Nullable Object... args);

    void info(@Nullable String text, @Nullable Throwable e, @Nullable Object... args);

    void debug(@Nullable String text, @Nullable Object... args);

    void debug(@Nullable String text, @Nullable Throwable e, @Nullable Object... args);
}
