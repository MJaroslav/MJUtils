package com.github.mjaroslav.mjutils.mod.common.modular;

import com.github.mjaroslav.mjutils.configurator.impl.loader.AnnotationForgeConfiguratorsLoader;
import com.github.mjaroslav.mjutils.mod.lib.ModInfo;
import com.github.mjaroslav.mjutils.modular.Module;
import com.github.mjaroslav.mjutils.modular.ModuleLoader;
import com.github.mjaroslav.mjutils.modular.impl.ConfiguratorsModule;
import com.github.mjaroslav.mjutils.util.UtilsInteractions;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nonnull;

@Module(ModInfo.MOD_ID)
public class ConfigurationModule extends ConfiguratorsModule {
    public static final String NAME = "configuration";

    public static AnnotationForgeConfiguratorsLoader loader;

    public ConfigurationModule(@Nonnull ModuleLoader loader) {
        super(loader, NAME);
        ConfigurationModule.loader = new AnnotationForgeConfiguratorsLoader(ModInfo.MOD_ID, true, true);
    }

    @Override
    public void construction(FMLConstructionEvent event) {
        loader.makeConfigurators(event);
    }

    @Override
    public void preInitialization(FMLPreInitializationEvent event) {
        loader.load();
    }
}
