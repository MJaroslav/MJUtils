package com.github.mjaroslav.mjutils.util.logging.impl;

import com.github.mjaroslav.mjutils.util.logging.ModLoggerLevel;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SystemModLogger extends ModLoggerAdapter {
    public SystemModLogger(@Nonnull String name) {
        super(name);
    }

    @Override
    public void log(@Nonnull ModLoggerLevel level, @Nullable String text, @Nullable Throwable e, @Nullable Object... args) {
        String message = StringUtils.isBlank(text) ? String.format("[%s]", level.name()) : String.format("[%s] %s", level.name(), text);
        message = String.format("[%s] %s", getName(), message);
        System.out.println(args == null ? message : String.format(message, args));
        if (e != null)
            e.printStackTrace();
    }
}
