package com.github.mjaroslav.mjutils.mod.lib;

import com.github.mjaroslav.mjutils.util.logging.ModLogger;
import com.github.mjaroslav.mjutils.util.logging.UtilsLogger;
import com.github.mjaroslav.mjutils.util.logging.impl.SystemModLogger;

public class TestModInfo {
    public static final ModLogger testLogger = UtilsLogger.getLoggerWithLevel(SystemModLogger.class, "tests");
}
