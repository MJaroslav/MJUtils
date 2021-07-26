package com.github.mjaroslav.mjutils.modular.impl;

import com.github.mjaroslav.mjutils.modular.Proxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public abstract class ProxyAdapter implements Proxy {
    @Nonnull
    @Override
    public String getName() {
        return PROXY_NAME_PREFIX + FMLCommonHandler.instance().getEffectiveSide().name();
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

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}
