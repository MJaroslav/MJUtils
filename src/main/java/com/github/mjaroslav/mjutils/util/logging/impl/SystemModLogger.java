package com.github.mjaroslav.mjutils.util.logging.impl;

import com.github.mjaroslav.mjutils.util.logging.ModLoggerLevel;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SystemModLogger extends ModLoggerAdapter {
    public SystemModLogger(@NotNull String name) {
        super(name);
    }

    @Override
    public void log(@NotNull ModLoggerLevel level, @Nullable String text, @Nullable Throwable e,
                    @Nullable Object... args) {
        var message = StringUtils.isBlank(text) ? String.format("[%s]", level.name())
            : String.format("[%s] %s", level.name(), text);
        message = String.format("[%s] %s", getName(), message);
        System.out.println(args == null ? message : String.format(message, args));
        if (e != null) e.printStackTrace();
    }
}
