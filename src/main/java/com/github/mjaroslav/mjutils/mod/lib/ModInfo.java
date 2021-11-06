package com.github.mjaroslav.mjutils.mod.lib;

import com.github.mjaroslav.mjutils.util.logging.ModLogger;
import com.github.mjaroslav.mjutils.util.logging.ModLoggerLevel;
import com.github.mjaroslav.mjutils.util.logging.UtilsLogger;
import com.github.mjaroslav.mjutils.util.logging.impl.Log4j2ModLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModInfo {
    public static final String modId = "mjutils";
    public static final String name = "MJUtils";
    public static final String version = "@VERSION@";
    public static final String guiFactory = "com.github.mjaroslav.mjutils.mod.client.gui.GUIFactory";

    public static final String clientProxy = "com.github.mjaroslav.mjutils.mod.client.ClientProxy";
    public static final String serverProxy = "com.github.mjaroslav.mjutils.mod.server.ServerProxy";

    public static final Logger log = LogManager.getLogger("PLACEHOLDER"); // TODO: Replace by ModLogger

    public static final ModLogger logger = UtilsLogger.getLoggerWithLevel(Log4j2ModLogger.class, name);
    public static final ModLogger loggerHooks = UtilsLogger.getLoggerWithLevel(Log4j2ModLogger.class, name + " HOOKS", ModLoggerLevel.OFF);
    public static final ModLogger loggerModules = UtilsLogger.getLoggerWithLevel(Log4j2ModLogger.class, name + " MODULES", ModLoggerLevel.ERROR);
}
