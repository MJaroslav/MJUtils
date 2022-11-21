package io.github.mjaroslav.mjutils.internal.common.modular.compat;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.LoaderState.ModState;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import io.github.mjaroslav.mjutils.internal.common.listener.ThaumEventListener;
import io.github.mjaroslav.mjutils.modular.SubscribeModule;
import io.github.mjaroslav.mjutils.util.game.compat.UtilsThaumcraft;

@SubscribeModule(loadOn = ModState.PREINITIALIZED, priority = 10, modDependencies = "Thaumcraft")
public class ThaumcraftModule {
    public void listen(FMLPreInitializationEvent event) {
        UtilsThaumcraft.activate();
    }

    public void listen(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(ThaumEventListener.instance);
    }
}
