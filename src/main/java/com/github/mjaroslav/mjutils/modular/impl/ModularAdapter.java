package com.github.mjaroslav.mjutils.modular.impl;

import com.github.mjaroslav.mjutils.modular.Modular;
import cpw.mods.fml.common.event.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ModularAdapter implements Modular {
    protected Modular parent;
    @Nonnull
    protected final String NAME;
    protected final int PRIORITY;
    protected final List<Modular> SUBMODULES;

    public ModularAdapter(@Nullable Modular parent, @Nonnull String name, int priority) {
        this.parent = parent;
        NAME = name;
        PRIORITY = priority;
        SUBMODULES = new ArrayList<>();
    }

    public ModularAdapter(@Nonnull String name, int priority) {
        this(null, name, priority);
    }

    public ModularAdapter(@Nullable Modular parent, @Nonnull String name) {
        this(parent, name, 0);
    }

    public ModularAdapter(@Nonnull String name) {
        this(null, name, 0);
    }

    @Nonnull
    @Override
    public String getName() {
        return NAME;
    }

    @Nullable
    @Override
    public Modular getParentModule() {
        return parent;
    }

    @Override
    public void setParentModule(@Nullable Modular parent) {
        this.parent = parent;
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
    public boolean isCanCrash() {
        return false;
    }

    @Override
    public boolean isSubmodule() {
        return parent != null;
    }

    @Override
    public boolean canLoad() {
        return true;
    }

    @Nonnull
    @Override
    public List<Modular> getSubModules() {
        return SUBMODULES;
    }

    @Override
    public void addSubModule(@Nonnull Modular module) {
        module.setParentModule(this);
        SUBMODULES.add(module);
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
}
