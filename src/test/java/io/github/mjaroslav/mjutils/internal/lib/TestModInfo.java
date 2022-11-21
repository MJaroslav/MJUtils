package io.github.mjaroslav.mjutils.internal.lib;

import io.github.mjaroslav.mjutils.util.logging.ModLogger;
import io.github.mjaroslav.mjutils.util.logging.UtilsLogger;
import io.github.mjaroslav.mjutils.util.logging.impl.Log4j2ModLogger;

public class TestModInfo {
    public static final ModLogger testLogger = UtilsLogger.getLoggerWithLevel(Log4j2ModLogger.class, "tests").wrapLevel(false);
}
