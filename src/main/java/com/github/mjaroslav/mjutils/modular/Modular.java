package com.github.mjaroslav.mjutils.modular;

import cpw.mods.fml.common.event.*;

import javax.annotation.Nonnull;
import java.util.List;

public interface Modular {
    int CONFIGURATORS_PRIORITY = Integer.MIN_VALUE;
    int PROXY_PRIORITY = Integer.MAX_VALUE;
    int DEFAULT_PRIORITY = 0;

    String PROXY_NAME_PREFIX = "PROXY@";
    String CONFIGURATOR_NAME_PREFIX = "CONFIGURATOR@";

    @Nonnull
    ModuleLoader getLoader();

    @Nonnull
    String getName();

    int getPriority();

    @Nonnull
    List<String> getModDependencies();

    @Nonnull
    List<String> getModuleDependencies();

    void preInitialization(FMLPreInitializationEvent event);

    void initialization(FMLInitializationEvent event);

    void postInitialization(FMLPostInitializationEvent event);

    void construction(FMLConstructionEvent event);

    void serverStopping(FMLServerStoppingEvent event);

    void serverStopped(FMLServerStoppedEvent event);

    void serverAboutToStart(FMLServerAboutToStartEvent event);

    void serverStarting(FMLServerStartingEvent event);

    void serverStarted(FMLServerStartedEvent event);

    void loadComplete(FMLLoadCompleteEvent event);

    void communications(FMLInterModComms.IMCEvent event);
}