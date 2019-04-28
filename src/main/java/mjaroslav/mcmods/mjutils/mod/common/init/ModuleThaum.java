package mjaroslav.mcmods.mjutils.mod.common.init;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.*;
import mjaroslav.mcmods.mjutils.lib.handler.ThaumEventHandler;
import mjaroslav.mcmods.mjutils.lib.module.Modular;
import mjaroslav.mcmods.mjutils.lib.module.Module;
import mjaroslav.mcmods.mjutils.lib.util.UtilsThaum;
import mjaroslav.mcmods.mjutils.mod.lib.ModInfo;

@Module(modid = ModInfo.MODID)
public class ModuleThaum implements Modular {
    @Override
    public String getModuleName() {
        return "Thaum";
    }

    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        UtilsThaum.activate();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new ThaumEventHandler());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
    }

    @Override
    public String[] modDependencies() {
        return new String[] { "Thaumcraft" };
    }

    @Override
    public boolean canLoad() {
        return true;
    }
}
