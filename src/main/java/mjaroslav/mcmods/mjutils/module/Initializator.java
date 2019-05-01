package mjaroslav.mcmods.mjutils.module;

import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * Design for calls inside FML events. Simply call
 * each method in its corresponding event. Can be
 * used alone or use {@link Modular}
 *
 * @see Modular
 */
public interface Initializator {
    /**
     * @param event event object from owner mod.
     */
    default void preInit(FMLPreInitializationEvent event) {
    }

    /**
     * @param event event object from owner mod.
     */
    default void init(FMLInitializationEvent event) {
    }

    /**
     * @param event event object from owner mod.
     */
    default void postInit(FMLPostInitializationEvent event) {
    }

    /**
     * @param event event object from owner mod.
     */
    default void construct(FMLConstructionEvent event) {
    }

    /**
     * Used for custom configurations that can be
     * connected to the {@link ModuleSystem}
     *
     * @see ModuleSystem
     */
    interface Configurable extends Initializator {
    }
}
