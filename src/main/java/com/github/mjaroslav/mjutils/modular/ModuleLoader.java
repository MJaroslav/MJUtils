package com.github.mjaroslav.mjutils.modular;

import cpw.mods.fml.common.event.*;

public class ModuleLoader {
    protected final String MOD_ID;

    public ModuleLoader(String modId) {
        MOD_ID = modId;
    }

    public String getModId() {
        return MOD_ID;
    }

    public void preInitialization(FMLPreInitializationEvent event) {
        System.out.println("############################################" + event);
    }

    public void initialization(FMLInitializationEvent event) {
        System.out.println("############################################" + event);
    }

    public void postInitialization(FMLPostInitializationEvent event) {
        System.out.println("############################################" + event);
    }

    public void construction(FMLConstructionEvent event) {
        System.out.println("############################################" + event);
        ;
    }

    public void serverStopping(FMLServerStoppingEvent event) {
        System.out.println("############################################" + event);
    }

    public void serverStopped(FMLServerStoppedEvent event) {
        System.out.println("############################################" + event);
    }

    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
        System.out.println("############################################" + event);
    }

    public void serverStarting(FMLServerStartingEvent event) {
        System.out.println("############################################" + event);
    }

    public void serverStarted(FMLServerStartedEvent event) {
        System.out.println("############################################" + event);
    }

    public void loadComplete(FMLLoadCompleteEvent event) {
        System.out.println("############################################" + event);
    }
}
