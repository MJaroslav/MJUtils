package com.github.mjaroslav.mjutils.mod.lib;

import com.github.mjaroslav.mjutils.util.logging.ModLogger;
import com.github.mjaroslav.mjutils.util.logging.ModLoggerLevel;
import com.github.mjaroslav.mjutils.util.logging.UtilsLogger;
import com.github.mjaroslav.mjutils.util.logging.impl.Log4j2ModLogger;

public class ModInfo {
    public static final String modId = "mjutils";
    public static final String name = "MJUtils";
    public static final String version = "@VERSION@";
    public static final String guiFactory = "com.github.mjaroslav.mjutils.mod.client.gui.GUIFactory";

    public static final String clientProxy = "com.github.mjaroslav.mjutils.mod.client.ClientProxy";
    public static final String serverProxy = "com.github.mjaroslav.mjutils.mod.server.ServerProxy";

    public static final ModLogger logger = UtilsLogger.getLoggerWithLevel(Log4j2ModLogger.class, name + "/mod");
    public static final ModLogger loggerLibrary = UtilsLogger.getLoggerWithLevel(Log4j2ModLogger.class, name + "/library");
    public static final ModLogger loggerModules = UtilsLogger.getLoggerWithLevel(Log4j2ModLogger.class, name + "/modules", ModLoggerLevel.ERROR);
}
