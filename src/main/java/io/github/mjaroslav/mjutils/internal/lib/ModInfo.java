package io.github.mjaroslav.mjutils.internal.lib;

import io.github.mjaroslav.mjutils.util.logging.ModLogger;
import io.github.mjaroslav.mjutils.util.logging.ModLoggerLevel;
import io.github.mjaroslav.mjutils.util.logging.UtilsLogger;
import io.github.mjaroslav.mjutils.util.logging.impl.Log4j2ModLogger;

public class ModInfo {
    public static final String modId = "mjutils";
    public static final String name = "MJUtils";
    public static final String version = "@VERSION@";
    public static final String guiFactory = "io.github.mjaroslav.mjutils.internal.client.gui.GUIFactory";

    public static final String clientProxy = "io.github.mjaroslav.mjutils.internal.client.ClientProxy";
    public static final String serverProxy = "io.github.mjaroslav.mjutils.internal.server.ServerProxy";

    public static final ModLogger logger = UtilsLogger.getLoggerWithLevel(Log4j2ModLogger.class, name + "/mod");
    public static final ModLogger loggerLibrary = UtilsLogger.getLoggerWithLevel(Log4j2ModLogger.class, name + "/library");
    public static final ModLogger loggerModules = UtilsLogger.getLoggerWithLevel(Log4j2ModLogger.class, name + "/modules", ModLoggerLevel.ERROR);
}
