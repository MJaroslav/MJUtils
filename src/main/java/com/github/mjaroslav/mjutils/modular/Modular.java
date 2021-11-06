package com.github.mjaroslav.mjutils.modular;

import cpw.mods.fml.common.event.*;

public interface Modular {
    default void listen(FMLConstructionEvent event) {
    }

    default void listen(FMLPreInitializationEvent event) {
    }

    default void listen(FMLInitializationEvent event) {
    }

    default void listen(FMLPostInitializationEvent event) {
    }

    default void listen(FMLServerStoppingEvent event) {
    }

    default void listen(FMLServerStoppedEvent event) {
    }

    default void listen(FMLServerAboutToStartEvent event) {
    }

    default void listen(FMLServerStartingEvent event) {
    }

    default void listen(FMLServerStartedEvent event) {
    }

    default void listen(FMLLoadCompleteEvent event) {
    }

    default void listen(FMLInterModComms.IMCEvent event) {
    }

    default void listen(FMLModDisabledEvent event) {
    }
}
