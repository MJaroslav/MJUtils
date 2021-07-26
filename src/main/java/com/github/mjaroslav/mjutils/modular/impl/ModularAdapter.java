package com.github.mjaroslav.mjutils.modular.impl;

import com.github.mjaroslav.mjutils.modular.Modular;
import com.github.mjaroslav.mjutils.modular.ModuleLoader;
import cpw.mods.fml.common.event.*;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public abstract class ModularAdapter implements Modular {
    @Nonnull
    protected final String NAME;
    @Nonnull
    protected final ModuleLoader LOADER;

    protected final int PRIORITY;

    public ModularAdapter(@Nonnull ModuleLoader loader, @Nonnull String name, int priority) {
        NAME = name;
        PRIORITY = priority;
        LOADER = loader;
    }

    public ModularAdapter(@Nonnull ModuleLoader loader, @Nonnull String name) {
        this(loader, name, DEFAULT_PRIORITY);
    }

    @Nonnull
    @Override
    public ModuleLoader getLoader() {
        return LOADER;
    }

    @Nonnull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    @Nonnull
    @Override
    public List<String> getModDependencies() {
        return Collections.emptyList();
    }

    @Nonnull
    @Override
    public List<String> getModuleDependencies() {
        return Collections.emptyList();
    }

    @Override
    public void preInitialization(FMLPreInitializationEvent event) {
    }

    @Override
    public void initialization(FMLInitializationEvent event) {
    }

    @Override
    public void postInitialization(FMLPostInitializationEvent event) {
    }

    @Override
    public void construction(FMLConstructionEvent event) {
    }

    @Override
    public void serverStopping(FMLServerStoppingEvent event) {
    }

    @Override
    public void serverStopped(FMLServerStoppedEvent event) {
    }

    @Override
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {
    }

    @Override
    public void serverStarted(FMLServerStartedEvent event) {
    }

    @Override
    public void loadComplete(FMLLoadCompleteEvent event) {
    }

    @Override
    public void communications(FMLInterModComms.IMCEvent event) {
    }
}
