package mjaroslav.mcmods.mjutils.mod.lib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModInfo {
    public static final String MODID = "mjutils";
    public static final String NAME = "MJUtils";
    public static final String VERSION = "1.5.0";
    public static final String COMMONPROXY = "mjaroslav.mcmods.mjutils.mod.common.CommonProxy";
    public static final String CLIENTPROXY = "mjaroslav.mcmods.mjutils.mod.client.ClientProxy";
    public static final String GUIFACTORY = "mjaroslav.mcmods.mjutils.mod.client.gui.GUIFactory";

    public static Logger LOG = LogManager.getLogger(NAME);
}