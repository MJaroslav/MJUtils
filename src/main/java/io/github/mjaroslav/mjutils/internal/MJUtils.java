package io.github.mjaroslav.mjutils.internal;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import io.github.mjaroslav.mjutils.lib.MJUtilsInfo;
import io.github.mjaroslav.mjutils.modular.ModuleLoader;
import io.github.mjaroslav.mjutils.modular.Proxy;
import io.github.mjaroslav.mjutils.modular.SubscribeLoader;
import io.github.mjaroslav.mjutils.modular.SubscribeSidedOnlyModule;

@Mod(modid = MJUtilsInfo.MOD_ID, name = MJUtilsInfo.NAME, version = MJUtilsInfo.VERSION,
    guiFactory = MJUtilsInfo.GUI_FACTORY, dependencies = MJUtilsInfo.DEPENDENCIES)
public class MJUtils {
    @SubscribeSidedOnlyModule
    @SidedProxy(clientSide = MJUtilsInfo.CLIENT_PROXY, serverSide = MJUtilsInfo.SERVER_PROXY)
    public static Proxy proxy;

    @SubscribeLoader
    public static ModuleLoader loader;

    @Instance
    public static MJUtils instance;
}
