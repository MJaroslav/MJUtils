package mjaroslav.mcmods.mjutils.mod.common.init;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mjaroslav.mcmods.mjutils.mod.common.handler.ThaumEventHandler;
import mjaroslav.mcmods.mjutils.mod.lib.ModInfo;
import mjaroslav.mcmods.mjutils.module.Modular;
import mjaroslav.mcmods.mjutils.module.Module;
import mjaroslav.mcmods.mjutils.util.UtilsThaum;

@Module(ModInfo.MODID)
public class ModuleThaum implements Modular {
    @Override
    public int priority() {
        return 10;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        UtilsThaum.activate();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(ThaumEventHandler.instance);
    }

    @Override
    public String[] dependencies() {
        return new String[]{"Thaumcraft"};
    }
}
