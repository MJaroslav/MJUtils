package mjaroslav.mcmods.mjutils.mod.common.init;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import mjaroslav.mcmods.mjutils.handler.FuelHandler;
import mjaroslav.mcmods.mjutils.handler.ReactionEventHandler;
import mjaroslav.mcmods.mjutils.handler.TooltipEventHandler;
import mjaroslav.mcmods.mjutils.module.ConfigurationBase.ConfigurationEventHandler;
import mjaroslav.mcmods.mjutils.module.Modular;
import mjaroslav.mcmods.mjutils.module.Module;
import mjaroslav.mcmods.mjutils.mod.lib.ModInfo;
import net.minecraftforge.common.MinecraftForge;

@Module(modid = ModInfo.MODID)
public class ModuleMain implements Modular {
    @Override
    public String name() {
        return "Main";
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
    }

    @Override
    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(ConfigurationEventHandler.instance);
        MinecraftForge.EVENT_BUS.register(new ReactionEventHandler());
        MinecraftForge.EVENT_BUS.register(new TooltipEventHandler());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        GameRegistry.registerFuelHandler(new FuelHandler());
    }

    @Override
    public String[] dependencies() {
        return null;
    }

    @Override
    public boolean canLoad() {
        return true;
    }
}
