package io.github.mjaroslav.mjutils.mod.lib;

import io.github.mjaroslav.mjutils.util.logging.ModLogger;
import io.github.mjaroslav.mjutils.util.logging.UtilsLogger;
import io.github.mjaroslav.mjutils.util.logging.impl.SystemModLogger;

public class TestModInfo {
    public static final ModLogger testLogger = UtilsLogger.getLoggerWithLevel(SystemModLogger.class, "tests");
}
