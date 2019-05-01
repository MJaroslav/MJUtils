package mjaroslav.mcmods.mjutils.mod.common.modules;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import mjaroslav.mcmods.mjutils.mod.common.handler.FuelHandler;
import mjaroslav.mcmods.mjutils.mod.common.handler.ReactionEventHandler;
import mjaroslav.mcmods.mjutils.mod.common.handler.TooltipEventHandler;
import mjaroslav.mcmods.mjutils.mod.lib.CategoryGeneral;
import mjaroslav.mcmods.mjutils.mod.lib.ModInfo;
import mjaroslav.mcmods.mjutils.module.FileBasedConfiguration.ConfigurationEventHandler;
import mjaroslav.mcmods.mjutils.module.Modular;
import mjaroslav.mcmods.mjutils.module.Module;
import mjaroslav.mcmods.mjutils.util.UtilsInteractions;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;

@Module(ModInfo.MODID)
public class ModuleMain implements Modular {
    @Override
    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(ConfigurationEventHandler.instance);
        MinecraftForge.EVENT_BUS.register(ReactionEventHandler.instance);
        MinecraftForge.EVENT_BUS.register(TooltipEventHandler.instance);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        GameRegistry.registerFuelHandler(FuelHandler.instance);
        if (CategoryGeneral.quartzTrigger)
            UtilsInteractions.setPigmanTriggerBlock(Blocks.quartz_ore, true);
    }
}
