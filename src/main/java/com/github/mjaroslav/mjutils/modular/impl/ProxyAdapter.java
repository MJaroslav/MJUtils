package com.github.mjaroslav.mjutils.modular.impl;

import com.github.mjaroslav.mjutils.modular.Modular;
import com.github.mjaroslav.mjutils.modular.Proxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public abstract class ProxyAdapter implements Proxy {
    @Nonnull
    @Override
    public String getName() {
        return PROXY_NAME_PREFIX + FMLCommonHandler.instance().getEffectiveSide().name();
    }

    @Nullable
    @Override
    public Modular getParentModule() {
        return null;
    }

    @Override
    public void setParentModule(@Nullable Modular parent) {

    }

    @Override
    public int getPriority() {
        return PROXY_PRIORITY;
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
        return false;
    }

    @Override
    public boolean canLoad() {
        return true;
    }

    @Nonnull
    @Override
    public List<Modular> getSubModules() {
        return Collections.emptyList();
    }

    @Override
    public void addSubModule(@Nonnull Modular module) {
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
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}
