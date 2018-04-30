package mjaroslav.mcmods.mjutils.lib.module;

import cpw.mods.fml.common.event.*;

public interface IInit {
    public void preInit(FMLPreInitializationEvent event);
    public void init(FMLInitializationEvent event);
    public void postInit(FMLPostInitializationEvent event);
}
