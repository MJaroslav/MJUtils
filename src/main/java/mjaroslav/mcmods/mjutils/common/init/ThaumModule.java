package mjaroslav.mcmods.mjutils.common.init;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mjaroslav.mcmods.mjutils.MJInfo;
import mjaroslav.mcmods.mjutils.common.objects.IModModule;
import mjaroslav.mcmods.mjutils.common.objects.ModInitModule;
import mjaroslav.mcmods.mjutils.common.thaum.ThaumEventHandler;
import mjaroslav.mcmods.mjutils.common.thaum.ThaumUtils;

@ModInitModule(modid = MJInfo.MODID)
public class ThaumModule implements IModModule {
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
        ThaumUtils.check();
        if (ThaumUtils.exist()) {

        }
    }

    @Override
    public void init(FMLInitializationEvent event) {
        if (ThaumUtils.exist()) {
            FMLCommonHandler.instance().bus().register(new ThaumEventHandler());
        }
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        if (ThaumUtils.exist()) {

        }
    }
}
