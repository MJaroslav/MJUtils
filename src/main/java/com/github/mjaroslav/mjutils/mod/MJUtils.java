package com.github.mjaroslav.mjutils.mod;

import com.github.mjaroslav.mjutils.mod.lib.ModInfo;
import com.github.mjaroslav.mjutils.module.AnnotationBasedConfiguration;
import com.github.mjaroslav.mjutils.module.ModuleSystem;
import com.github.mjaroslav.mjutils.util.UtilsInteractions;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.NAME, version = ModInfo.VERSION, guiFactory = ModInfo.GUI_FACTORY)
public class MJUtils {
    @Instance(ModInfo.MOD_ID)
    public static MJUtils instance;

    public static final AnnotationBasedConfiguration CONFIG = new AnnotationBasedConfiguration(ModInfo.MOD_ID, ModInfo.LOG);

    private static ModuleSystem initHandler;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        initHandler.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        initHandler.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        initHandler.postInit(event);
    }

    @EventHandler
    public void constr(FMLConstructionEvent event) {
        initHandler = new ModuleSystem(ModInfo.MOD_ID, CONFIG, null);
        initHandler.initSystem(event);
    }

    @EventHandler
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
