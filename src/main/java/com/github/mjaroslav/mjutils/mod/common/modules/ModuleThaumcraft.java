package com.github.mjaroslav.mjutils.mod.common.modules;

import com.github.mjaroslav.mjutils.module.Modular;
import com.github.mjaroslav.mjutils.module.Module;
import com.github.mjaroslav.mjutils.util.UtilsThaumcraft;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import com.github.mjaroslav.mjutils.mod.common.handler.ThaumEventHandler;
import com.github.mjaroslav.mjutils.mod.lib.ModInfo;

@Module(ModInfo.MOD_ID)
public class ModuleThaumcraft implements Modular {
    @Override
    public int priority() {
        return 10;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        UtilsThaumcraft.activate();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(ThaumEventHandler.instance);
    }

    @Override
    public String[] dependencies() {
        return new String[]{"Thaumcraft"};
    }
}
