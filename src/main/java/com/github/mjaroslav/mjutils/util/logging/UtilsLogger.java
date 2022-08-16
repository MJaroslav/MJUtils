package com.github.mjaroslav.mjutils.util.logging;

import javax.annotation.Nonnull;

public class UtilsLogger {
    public static ModLogger getLoggerWithLevel(@Nonnull Class<? extends ModLogger> loggerClass, @Nonnull String name, @Nonnull ModLoggerLevel defaultLevel) {
        String parsed = System.getProperty(String.format("logging.%s.level", name.toLowerCase().replace(' ', '.').replace('/', '.')), defaultLevel.name());
        ModLoggerLevel level = ModLoggerLevel.getByName(parsed);
        try {
            ModLogger logger = loggerClass.getConstructor(String.class).newInstance(name);
            logger.setLevel(level);
            return logger;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error on getting ModLogger with class " + loggerClass.getName());
        }
    }

    public static ModLogger getLoggerWithLevel(@Nonnull Class<? extends ModLogger> loggerClass, @Nonnull String name) {
        return getLoggerWithLevel(loggerClass, name, ModLoggerLevel.INFO);
    }
}
