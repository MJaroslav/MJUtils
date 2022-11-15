package com.github.mjaroslav.mjutils.util.logging.impl;

import com.github.mjaroslav.mjutils.util.logging.ModLoggerLevel;
import lombok.Getter;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Log4j2ModLogger extends ModLoggerAdapter {
    protected final Logger logger;
    @Getter
    protected boolean wrapLevel = true;

    public Log4j2ModLogger(@NotNull String name) {
        super(name);
        logger = LogManager.getLogger(name);
    }

    @Override
    public void log(@NotNull ModLoggerLevel level, @Nullable String text, @Nullable Throwable e,
                    @Nullable Object... args) {
        val message = StringUtils.isBlank(text) ? String.format("[%s]", level.name())
            : String.format("[%s] %s", level.name(), text);
        if (e == null) logger.log(level(level), String.format(message, args));
        else logger.log(level(level), String.format(message, args), e);
    }

    public Log4j2ModLogger wrapLevel(boolean value) {
        wrapLevel = value;
        return this;
    }

    protected @NotNull Level level(@NotNull ModLoggerLevel level) {
        if (wrapLevel) return Level.INFO;
        return switch (level) {
            case ALL -> Level.ALL;
            case DEBUG -> Level.DEBUG;
            case INFO, UNKNOWN -> Level.INFO;
            case WARN -> Level.WARN;
            case ERROR -> Level.ERROR;
            case OFF -> Level.OFF;
        };
    }
}
