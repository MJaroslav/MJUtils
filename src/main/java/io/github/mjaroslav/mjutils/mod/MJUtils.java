package io.github.mjaroslav.mjutils.mod;

import io.github.mjaroslav.mjutils.mod.lib.ModInfo;
import io.github.mjaroslav.mjutils.modular.ModuleLoader;
import io.github.mjaroslav.mjutils.modular.Proxy;
import io.github.mjaroslav.mjutils.modular.SubscribeLoader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;

@Mod(modid = ModInfo.modId, name = ModInfo.name, version = ModInfo.version,
        guiFactory = ModInfo.guiFactory)
public class MJUtils {
    @SidedProxy(clientSide = ModInfo.clientProxy, serverSide = ModInfo.serverProxy)
    public static Proxy proxy;

    @SubscribeLoader
    public static ModuleLoader loader;

    @Instance
    public static MJUtils instance;
}
