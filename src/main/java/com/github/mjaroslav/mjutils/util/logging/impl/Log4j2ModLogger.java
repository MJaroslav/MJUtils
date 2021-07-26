package com.github.mjaroslav.mjutils.util.logging.impl;

import com.github.mjaroslav.mjutils.util.logging.ModLoggerLevel;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Log4j2ModLogger extends ModLoggerAdapter {
    protected final Logger logger;

    public Log4j2ModLogger(@Nonnull String name) {
        super(name);
        logger = LogManager.getLogger(name);
    }

    @Override
    public void log(@Nonnull ModLoggerLevel level, @Nullable String text, @Nullable Throwable e, @Nullable Object... args) {
        String message = StringUtils.isBlank(text) ? String.format("[%s]", level.name()) : String.format("[%s] %s", level.name(), text);
        if (e == null)
            logger.log(Level.INFO, String.format(message, args));
        else
            logger.log(Level.INFO, String.format(message, args), e);
    }
}
