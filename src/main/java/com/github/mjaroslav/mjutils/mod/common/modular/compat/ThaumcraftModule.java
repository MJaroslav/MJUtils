package com.github.mjaroslav.mjutils.mod.common.modular.compat;

import com.github.mjaroslav.mjutils.mod.common.handler.ThaumEventHandler;
import com.github.mjaroslav.mjutils.mod.lib.ModInfo;
import com.github.mjaroslav.mjutils.modular.Module;
import com.github.mjaroslav.mjutils.modular.ModuleLoader;
import com.github.mjaroslav.mjutils.modular.impl.ModularAdapter;
import com.github.mjaroslav.mjutils.util.UtilsThaumcraft;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nonnull;
import java.util.List;

@Module(ModInfo.MOD_ID)
public class ThaumcraftModule extends ModularAdapter {
    public static final String NAME = "thaumcraft";

    public ThaumcraftModule(@Nonnull ModuleLoader loader) {
        super(loader, NAME);
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public void preInitialization(FMLPreInitializationEvent event) {
        UtilsThaumcraft.activate();
    }

    @Override
    public void initialization(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(ThaumEventHandler.instance);
    }

    @Nonnull
    @Override
    public List<String> getModDependencies() {
        return Lists.newArrayList("Thaumcraft");
    }
}
