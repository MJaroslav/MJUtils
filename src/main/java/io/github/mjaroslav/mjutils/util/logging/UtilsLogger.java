package io.github.mjaroslav.mjutils.util.logging;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class UtilsLogger {
    public @NotNull <T extends ModLogger> T getLoggerWithLevel(@NotNull Class<T> loggerClass, @NotNull String name,
                                                               @NotNull ModLoggerLevel defaultLevel) {
        val parsed = System.getProperty(String.format("logging.%s.level", name.toLowerCase().replace(' ', '.')
            .replace('/', '.')), defaultLevel.name());
        val level = ModLoggerLevel.getByName(parsed);
        try {
            val logger = loggerClass.getConstructor(String.class).newInstance(name);
            logger.setLevel(level);
            return logger;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error on getting ModLogger with class " + loggerClass.getName());
        }
    }

    public @NotNull <T extends ModLogger> T getLoggerWithLevel(@NotNull Class<T> loggerClass, @NotNull String name) {
        return getLoggerWithLevel(loggerClass, name, ModLoggerLevel.INFO);
    }
}
