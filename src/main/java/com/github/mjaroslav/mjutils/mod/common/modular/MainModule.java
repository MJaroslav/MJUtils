package com.github.mjaroslav.mjutils.mod.common.modular;

import com.github.mjaroslav.mjutils.mod.common.handler.FuelHandler;
import com.github.mjaroslav.mjutils.mod.common.handler.ReactionEventHandler;
import com.github.mjaroslav.mjutils.mod.common.handler.TooltipEventHandler;
import com.github.mjaroslav.mjutils.mod.lib.CategoryGeneral;
import com.github.mjaroslav.mjutils.mod.lib.ModInfo;
import com.github.mjaroslav.mjutils.modular.Module;
import com.github.mjaroslav.mjutils.modular.ModuleLoader;
import com.github.mjaroslav.mjutils.modular.impl.ModularAdapter;
import com.github.mjaroslav.mjutils.module.FileBasedConfiguration;
import com.github.mjaroslav.mjutils.util.UtilsInteractions;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nonnull;

@Module(ModInfo.MOD_ID)
public class MainModule extends ModularAdapter {
    public static final String NAME = "main";

    public MainModule(@Nonnull ModuleLoader loader) {
        super(loader, NAME);
    }

    @Override
    public void initialization(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(FileBasedConfiguration.ConfigurationEventHandler.instance);
        MinecraftForge.EVENT_BUS.register(ReactionEventHandler.instance);
        MinecraftForge.EVENT_BUS.register(TooltipEventHandler.instance);
    }

    @Override
    public void postInitialization(FMLPostInitializationEvent event) {
        GameRegistry.registerFuelHandler(FuelHandler.instance);
        if (CategoryGeneral.quartzTrigger)
            UtilsInteractions.setPigmanTriggerBlock(Blocks.quartz_ore, true);
    }

    @Override
    public void communications(FMLInterModComms.IMCEvent event) {
        for (FMLInterModComms.IMCMessage message : event.getMessages())
            switch (message.key) {
                case "interactions.block_in_creative": {
                    if (message.isItemStackMessage()) {
                        UtilsInteractions.setDisableBlockBreakingInCreative(message.getItemStackValue(), true);
                    } else ModInfo.LOG.error("IMC@interactions.block_in_creative: value must be ItemStack.");
                }
                break;
                case "interactions.unblock_in_creative": {
                    if (message.isItemStackMessage()) {
                        UtilsInteractions.setDisableBlockBreakingInCreative(message.getItemStackValue(), false);
                    } else ModInfo.LOG.error("IMC@interactions.unblock_in_creative: value must be ItemStack.");
                }
                break;
                case "interactions.pigzombie_trigger_block": {
                    if (message.isItemStackMessage()) {
                        UtilsInteractions.setPigmanTriggerBlock(message.getItemStackValue(), true);
                    } else ModInfo.LOG.error("IMC@interactions.pigzombie_trigger_block: value must be ItemStack.");
                }
                break;
                case "interactions.not_pigzombie_trigger_block": {
                    if (message.isItemStackMessage()) {
                        UtilsInteractions.setPigmanTriggerBlock(message.getItemStackValue(), false);
                    } else ModInfo.LOG.error("IMC@interactions.not_pigzombie_trigger_block: value must be ItemStack.");
                }
                break;
                default:
                    ModInfo.LOG.warn(String.format("Unknown IMC message: %s", message.key));
            }
    }
}
