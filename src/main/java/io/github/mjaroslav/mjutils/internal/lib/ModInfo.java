package io.github.mjaroslav.mjutils.internal.lib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModInfo {
    public static final String modId = "mjutils";
    public static final String name = "MJUtils";
    public static final String version = "@VERSION@";
    public static final String guiFactory = "io.github.mjaroslav.mjutils.internal.client.gui.GUIFactory";

    public static final String clientProxy = "io.github.mjaroslav.mjutils.internal.client.ClientProxy";
    public static final String serverProxy = "io.github.mjaroslav.mjutils.internal.server.ServerProxy";

    public static final Logger LOG_PATCHING = LogManager.getLogger(name + "/Patching");
    public static final Logger LOG_IMC = LogManager.getLogger(name + "/IMC");
    public static final Logger LOG_LIB = LogManager.getLogger(name + "/Lib");
    public static final Logger LOG_MODULES = LogManager.getLogger(name + "/Modules");
}
