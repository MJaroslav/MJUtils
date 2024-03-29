package io.github.mjaroslav.mjutils.internal.common.modular;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import io.github.mjaroslav.mjutils.config.ForgeAnnotationConfig;
import io.github.mjaroslav.mjutils.config.ForgeConfig.ForgeConfigEventHandler;
import io.github.mjaroslav.mjutils.internal.client.listener.ClientWorldEventListener;
import io.github.mjaroslav.mjutils.internal.common.listener.BlockEventListener;
import io.github.mjaroslav.mjutils.internal.common.listener.FuelHandler;
import io.github.mjaroslav.mjutils.internal.common.listener.TooltipEventListener;
import io.github.mjaroslav.mjutils.item.Stacks;
import io.github.mjaroslav.mjutils.lib.General;
import io.github.mjaroslav.mjutils.lib.MJUtilsInfo;
import io.github.mjaroslav.mjutils.modular.Proxy;
import io.github.mjaroslav.mjutils.modular.SubscribeModule;
import io.github.mjaroslav.mjutils.util.game.UtilsInteractions;
import io.github.mjaroslav.mjutils.util.game.UtilsMods;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;

@SubscribeModule
public class MainModule {
    public static final ForgeAnnotationConfig CONFIG = new ForgeAnnotationConfig(MJUtilsInfo.MOD_ID,
        Paths.get("config", MJUtilsInfo.MOD_ID, "general.cfg"), "2.0.0", General.class);

    public void listen(@NotNull FMLInitializationEvent event) {
        CONFIG.registerSyncCallback(() -> UtilsInteractions.configureBlockAsPigZombieGreedTrigger(Blocks.quartz_ore,
            General.quartzCausePigsGreedAttack));
        CONFIG.load();
        FMLCommonHandler.instance().bus().register(ForgeConfigEventHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(BlockEventListener.INSTANCE);
        MinecraftForge.EVENT_BUS.register(TooltipEventListener.instance);
    }

    public void listen(@NotNull FMLPostInitializationEvent event) {
        GameRegistry.registerFuelHandler(FuelHandler.INSTANCE);
        UtilsMods.init();
    }

    public void listen(@NotNull FMLInterModComms.IMCEvent event) {
        for (var message : event.getMessages())
            if (Proxy.isClient()) { // Separate client messages for NoClassFound dodging.
                if ("disable_block_destroying_in_creative".equals(message.key)) {
                    if (message.isItemStackMessage())
                        ClientWorldEventListener.INSTANCE.extraDisabledForCreativeDestroying
                            .add(message.getItemStackValue());
                    else if (message.isStringMessage())
                        ClientWorldEventListener.INSTANCE.extraDisabledForCreativeDestroying
                            .add(Stacks.make(message.key));
                    else
                        MJUtilsInfo.LOG_IMC.error("IMC@disable_block_destroying_in_creative: value must be ItemStack " +
                            "or registry name.");
                } else MJUtilsInfo.LOG_IMC.warn(String.format("Unknown IMC message: %s", message.key));
            } else {
                if ("make_block_treasure_for_pig_zombies".equals(message.key)) {
                    if (message.isStringMessage())
                        UtilsInteractions.configureBlockAsPigZombieGreedTrigger(Stacks
                            .make(message.getStringValue()), true);
                    else if (message.isItemStackMessage())
                        UtilsInteractions.configureBlockAsPigZombieGreedTrigger(message.getItemStackValue(), true);
                    else MJUtilsInfo.LOG_IMC.error("IMC@make_block_treasure_for_pig_zombies: value must be ItemStack " +
                            "or registry name.");
                } else MJUtilsInfo.LOG_IMC.warn(String.format("Unknown IMC message: %s", message.key));
            }
    }
}
