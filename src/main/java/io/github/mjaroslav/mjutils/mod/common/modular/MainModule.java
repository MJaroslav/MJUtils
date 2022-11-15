package io.github.mjaroslav.mjutils.mod.common.modular;

import io.github.mjaroslav.mjutils.config.ForgeAnnotationConfig;
import io.github.mjaroslav.mjutils.config.ForgeConfig.ForgeConfigEventHandler;
import io.github.mjaroslav.mjutils.mod.common.handler.FuelHandler;
import io.github.mjaroslav.mjutils.mod.common.handler.GuiReplacerEventHandler;
import io.github.mjaroslav.mjutils.mod.common.handler.ReactionEventHandler;
import io.github.mjaroslav.mjutils.mod.common.handler.TooltipEventHandler;
import io.github.mjaroslav.mjutils.mod.lib.General;
import io.github.mjaroslav.mjutils.mod.lib.ModInfo;
import io.github.mjaroslav.mjutils.modular.SubscribeModule;
import io.github.mjaroslav.mjutils.util.game.UtilsInteractions;
import io.github.mjaroslav.mjutils.util.game.UtilsMods;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;

@SubscribeModule(priority = -1)
public class MainModule {
    public static final ForgeAnnotationConfig config = new ForgeAnnotationConfig(ModInfo.modId,
        Paths.get("config", ModInfo.modId + ".cfg"), "0", General.class);

    public void listen(@NotNull FMLInitializationEvent event) {
        config.registerSyncCallback(() -> UtilsInteractions.setPigmanTriggerBlock(Blocks.quartz_block,
            General.quartzTrigger));
        config.load();
        FMLCommonHandler.instance().bus().register(ForgeConfigEventHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(ReactionEventHandler.instance);
        MinecraftForge.EVENT_BUS.register(TooltipEventHandler.instance);
        MinecraftForge.EVENT_BUS.register(GuiReplacerEventHandler.instance);
    }

    public void listen(@NotNull FMLPostInitializationEvent event) {
        GameRegistry.registerFuelHandler(FuelHandler.INSTANCE);
        UtilsInteractions.setPigmanTriggerBlock(Blocks.quartz_block, General.quartzTrigger);
        UtilsMods.init();
    }

    public void listen(@NotNull FMLInterModComms.IMCEvent event) {
        for (var message : event.getMessages())
            switch (message.key) {
                case "interactions.block_in_creative" -> {
                    if (message.isItemStackMessage())
                        UtilsInteractions.setDisabledForBlockBreakingInCreative(message.getItemStackValue(), true);
                    else ModInfo.logger.error("IMC@interactions.block_in_creative: value must be ItemStack.");
                }
                case "interactions.unblock_in_creative" -> {
                    if (message.isItemStackMessage())
                        UtilsInteractions.setDisabledForBlockBreakingInCreative(message.getItemStackValue(), false);
                    else ModInfo.logger.error("IMC@interactions.unblock_in_creative: value must be ItemStack.");
                }
                case "interactions.pigzombie_trigger_block" -> {
                    if (message.isItemStackMessage())
                        UtilsInteractions.setPigmanTriggerBlock(message.getItemStackValue(), true);
                    else ModInfo.logger.error("IMC@interactions.pigzombie_trigger_block: value must be ItemStack.");
                }
                case "interactions.not_pigzombie_trigger_block" -> {
                    if (message.isItemStackMessage())
                        UtilsInteractions.setPigmanTriggerBlock(message.getItemStackValue(), false);
                    else
                        ModInfo.logger.error("IMC@interactions.not_pigzombie_trigger_block: value must be ItemStack.");
                }
                default -> ModInfo.logger.warn(String.format("Unknown IMC message: %s", message.key));
            }
    }
}
