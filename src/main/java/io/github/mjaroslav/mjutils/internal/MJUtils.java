package io.github.mjaroslav.mjutils.internal;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import io.github.mjaroslav.mjutils.internal.lib.ModInfo;
import io.github.mjaroslav.mjutils.modular.ModuleLoader;
import io.github.mjaroslav.mjutils.modular.Proxy;
import io.github.mjaroslav.mjutils.modular.SubscribeLoader;

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
