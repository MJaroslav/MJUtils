package com.github.mjaroslav.mjutils.example;

import com.github.mjaroslav.mjutils.example.common.CommonProxy;
import com.github.mjaroslav.mjutils.example.lib.ModInfo;
import com.github.mjaroslav.mjutils.modular.ModuleLoader;
import com.github.mjaroslav.mjutils.modular.SubscribeLoader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;

@Mod(modid = ModInfo.modId, name = ModInfo.name, version = ModInfo.version, dependencies = ModInfo.dependencies,
        guiFactory = ModInfo.guiFactory)
public class MJUtilsExample {
    @SidedProxy(clientSide = ModInfo.clientProxy, serverSide = ModInfo.serverProxy)
    public static CommonProxy proxy;

    @SubscribeLoader
    public static ModuleLoader loader;
}
