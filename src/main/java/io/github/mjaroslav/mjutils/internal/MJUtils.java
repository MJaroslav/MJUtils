package io.github.mjaroslav.mjutils.internal;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import io.github.mjaroslav.mjutils.lib.ModInfo;
import io.github.mjaroslav.mjutils.modular.ModuleLoader;
import io.github.mjaroslav.mjutils.modular.Proxy;
import io.github.mjaroslav.mjutils.modular.SubscribeLoader;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.NAME, version = ModInfo.VERSION,
    guiFactory = ModInfo.GUI_FACTORY)
public class MJUtils {
    @SidedProxy(clientSide = ModInfo.CLIENT_PROXY, serverSide = ModInfo.SERVER_PROXY)
    public static Proxy proxy;

    @SubscribeLoader
    public static ModuleLoader loader;

    @Instance
    public static MJUtils instance;
}
