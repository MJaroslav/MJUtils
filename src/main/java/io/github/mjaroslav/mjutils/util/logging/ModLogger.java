package io.github.mjaroslav.mjutils.util.logging;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ModLogger {
    @NotNull String getName();

    @NotNull ModLoggerLevel getLevel();

    void setLevel(@NotNull ModLoggerLevel level);

    void log(@NotNull ModLoggerLevel level, @Nullable String text, @Nullable Object... args);

    void log(@NotNull ModLoggerLevel level, @Nullable String text, @Nullable Throwable e, @Nullable Object... args);

    void error(@Nullable String text, @Nullable Object... args);

    void error(@Nullable String text, @Nullable Throwable e, @Nullable Object... args);

    void warn(@Nullable String text, @Nullable Object... args);

    void warn(@Nullable String text, @Nullable Throwable e, @Nullable Object... args);

    void info(@Nullable String text, @Nullable Object... args);

    void info(@Nullable String text, @Nullable Throwable e, @Nullable Object... args);

    void debug(@Nullable String text, @Nullable Object... args);

    void debug(@Nullable String text, @Nullable Throwable e, @Nullable Object... args);
}
