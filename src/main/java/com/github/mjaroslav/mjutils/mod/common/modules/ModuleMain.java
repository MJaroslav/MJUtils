package com.github.mjaroslav.mjutils.mod.common.modules;

import com.github.mjaroslav.mjutils.module.FileBasedConfiguration;
import com.github.mjaroslav.mjutils.module.Modular;
import com.github.mjaroslav.mjutils.module.Module;
import com.github.mjaroslav.mjutils.util.UtilsInteractions;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import com.github.mjaroslav.mjutils.mod.common.handler.FuelHandler;
import com.github.mjaroslav.mjutils.mod.common.handler.ReactionEventHandler;
import com.github.mjaroslav.mjutils.mod.common.handler.TooltipEventHandler;
import com.github.mjaroslav.mjutils.mod.lib.CategoryGeneral;
import com.github.mjaroslav.mjutils.mod.lib.ModInfo;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;

@Module(ModInfo.MOD_ID)
public class ModuleMain implements Modular {
    @Override
    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(FileBasedConfiguration.ConfigurationEventHandler.instance);
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
