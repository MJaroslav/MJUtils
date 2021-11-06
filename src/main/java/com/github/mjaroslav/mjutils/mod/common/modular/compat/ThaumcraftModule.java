package com.github.mjaroslav.mjutils.mod.common.modular.compat;

import com.github.mjaroslav.mjutils.mod.common.handler.ThaumEventHandler;
import com.github.mjaroslav.mjutils.modular.Modular;
import com.github.mjaroslav.mjutils.modular.SubscribeModule;
import com.github.mjaroslav.mjutils.util.game.compat.UtilsThaumcraft;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.LoaderState.ModState;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@SubscribeModule(/*value = ModInfo.modId,*/ loadOn = ModState.PREINITIALIZED, priority = 10, modDependencies = "Thaumcraft")
public class ThaumcraftModule implements Modular {
    @Override
    public void listen(FMLPreInitializationEvent event) {
        UtilsThaumcraft.activate();
    }

    @Override
    public void listen(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(ThaumEventHandler.instance);
    }
}
