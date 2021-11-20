package com.github.mjaroslav.mjutils.mod.common.modular;

import com.github.mjaroslav.mjutils.configurator.AnnotationConfigurator;
import com.github.mjaroslav.mjutils.configurator.ForgeConfigurator;
import com.github.mjaroslav.mjutils.mod.common.handler.FuelHandler;
import com.github.mjaroslav.mjutils.mod.common.handler.GuiReplacerEventHandler;
import com.github.mjaroslav.mjutils.mod.common.handler.ReactionEventHandler;
import com.github.mjaroslav.mjutils.mod.common.handler.TooltipEventHandler;
import com.github.mjaroslav.mjutils.mod.lib.CategoryRoot;
import com.github.mjaroslav.mjutils.mod.lib.ModInfo;
import com.github.mjaroslav.mjutils.modular.SubscribeModule;
import com.github.mjaroslav.mjutils.util.game.UtilsInteractions;
import com.github.mjaroslav.mjutils.util.game.UtilsMods;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;

@SubscribeModule(priority = -1)
public class MainModule {
    public static final AnnotationConfigurator config = new AnnotationConfigurator(ModInfo.modId, ModInfo.modId, CategoryRoot.class);

    public void listen(FMLInitializationEvent event) {
        config.load();
        FMLCommonHandler.instance().bus().register(ForgeConfigurator.ConfigurationEventHandler.instance);
        MinecraftForge.EVENT_BUS.register(ReactionEventHandler.instance);
        MinecraftForge.EVENT_BUS.register(TooltipEventHandler.instance);
        MinecraftForge.EVENT_BUS.register(GuiReplacerEventHandler.instance);
    }

    public void listen(FMLPostInitializationEvent event) {
        GameRegistry.registerFuelHandler(FuelHandler.instance);
        if (CategoryRoot.quartzTrigger)
            UtilsInteractions.setPigmanTriggerBlock(Blocks.quartz_ore, true);
    }

    public void listen(FMLInterModComms.IMCEvent event) {
        for (FMLInterModComms.IMCMessage message : event.getMessages())
            switch (message.key) {
                case "interactions.block_in_creative": {
                    if (message.isItemStackMessage()) {
                        UtilsInteractions.setDisableBlockBreakingInCreative(message.getItemStackValue(), true);
                    } else ModInfo.log.error("IMC@interactions.block_in_creative: value must be ItemStack.");
                }
                break;
                case "interactions.unblock_in_creative": {
                    if (message.isItemStackMessage()) {
                        UtilsInteractions.setDisableBlockBreakingInCreative(message.getItemStackValue(), false);
                    } else ModInfo.log.error("IMC@interactions.unblock_in_creative: value must be ItemStack.");
                }
                break;
                case "interactions.pigzombie_trigger_block": {
                    if (message.isItemStackMessage()) {
                        UtilsInteractions.setPigmanTriggerBlock(message.getItemStackValue(), true);
                    } else ModInfo.log.error("IMC@interactions.pigzombie_trigger_block: value must be ItemStack.");
                }
                break;
                case "interactions.not_pigzombie_trigger_block": {
                    if (message.isItemStackMessage()) {
                        UtilsInteractions.setPigmanTriggerBlock(message.getItemStackValue(), false);
                    } else ModInfo.log.error("IMC@interactions.not_pigzombie_trigger_block: value must be ItemStack.");
                }
                break;
                case "modList.blockFromDisabling": {
                    UtilsMods.blockedForDisableMods.add(message.getSender());
                }
                break;
                default:
                    ModInfo.log.warn(String.format("Unknown IMC message: %s", message.key));
            }
    }
}
