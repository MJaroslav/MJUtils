package com.github.mjaroslav.mjutils.util.game;

import cpw.mods.fml.common.Loader;

public class UtilsMods {
    public static String getActiveModId() {
        return Loader.instance().activeModContainer().getModId();
    }

    public static boolean isModsLoaded(String... modIds) {
        for (String modId : modIds)
            if (!Loader.isModLoaded(modId))
                return false;
        return true;
    }
}
