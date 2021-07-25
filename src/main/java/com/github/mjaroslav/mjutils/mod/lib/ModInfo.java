package com.github.mjaroslav.mjutils.mod.lib;

import com.github.mjaroslav.mjutils.util.logging.ModLogger;
import com.github.mjaroslav.mjutils.util.logging.ModLoggerLevel;
import com.github.mjaroslav.mjutils.util.logging.UtilsLogger;
import com.github.mjaroslav.mjutils.util.logging.impl.Log4j2ModLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModInfo {
    public static final String MOD_ID = "mjutils";
    public static final String NAME = "MJUtils";
    public static final String VERSION = "@VERSION@";
    public static final String GUI_FACTORY = "com.github.mjaroslav.mjutils.mod.client.gui.GUIFactory";

    public static final Logger LOG = LogManager.getLogger("PLACEHOLDER"); // TODO: Replace by ModLogger

    public static final ModLogger LOGGER = UtilsLogger.getLoggerWithLevel(Log4j2ModLogger.class, NAME);
    public static final ModLogger LOGGER_HOOKS = UtilsLogger.getLoggerWithLevel(Log4j2ModLogger.class, NAME + " HOOKS", ModLoggerLevel.OFF);
    public static final ModLogger LOGGER_MODULES = UtilsLogger.getLoggerWithLevel(Log4j2ModLogger.class, NAME + " MODULES", ModLoggerLevel.OFF);
}
