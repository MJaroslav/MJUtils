package io.github.mjaroslav.mjutils.lib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModInfo {
    public static final String MOD_ID = "mjutils";
    public static final String NAME = "MJUtils";
    public static final String VERSION = "@VERSION@";
    public static final String GUI_FACTORY = "io.github.mjaroslav.mjutils.internal.client.gui.GUIFactory";

    public static final String CLIENT_PROXY = "io.github.mjaroslav.mjutils.internal.client.ClientProxy";
    public static final String SERVER_PROXY = "io.github.mjaroslav.mjutils.internal.server.ServerProxy";

    public static final Logger LOG_PATCHING = LogManager.getLogger(NAME + "/Patching");
    public static final Logger LOG_IMC = LogManager.getLogger(NAME + "/IMC");
    public static final Logger LOG_LIB = LogManager.getLogger(NAME + "/Lib");
    public static final Logger LOG_MODULES = LogManager.getLogger(NAME + "/Modules");
}
