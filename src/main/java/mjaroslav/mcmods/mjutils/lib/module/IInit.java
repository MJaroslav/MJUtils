package mjaroslav.mcmods.mjutils.lib.module;

import cpw.mods.fml.common.event.*;

public interface IInit {
    /**
     * Called in a similar method of the main modification class. Do not call
     * this method elsewhere!
     *
     * @param event
     *            - event from mail class.
     */
    public void preInit(FMLPreInitializationEvent event);

    /**
     * Called in a similar method of the main modification class. Do not call
     * this method elsewhere!
     *
     * @param event
     *            - event from mail class.
     */
    public void init(FMLInitializationEvent event);

    /**
     * Called in a similar method of the main modification class. Do not call
     * this method elsewhere!
     *
     * @param event
     *            - event from mail class.
     */
    public void postInit(FMLPostInitializationEvent event);
}
