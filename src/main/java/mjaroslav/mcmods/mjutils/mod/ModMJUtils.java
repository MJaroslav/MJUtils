package mjaroslav.mcmods.mjutils.mod;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
import mjaroslav.mcmods.mjutils.module.AnnotationBasedConfiguration;
import mjaroslav.mcmods.mjutils.module.ModuleSystem;
import mjaroslav.mcmods.mjutils.util.UtilsInteractions;

import static mjaroslav.mcmods.mjutils.mod.lib.ModInfo.*;

@Mod(modid = MODID, name = NAME, version = VERSION, guiFactory = GUI_FACTORY)
public class ModMJUtils {
    @Instance(MODID)
    public static ModMJUtils instance;

    public static final AnnotationBasedConfiguration CONFIG = new AnnotationBasedConfiguration(MODID, LOG);

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
        initHandler = new ModuleSystem(MODID, CONFIG, null);
        initHandler.initSystem(event);
    }

    @EventHandler
    public void communications(FMLInterModComms.IMCEvent event) {
        for (FMLInterModComms.IMCMessage message : event.getMessages())
            switch (message.key) {
                case "interactions.block_in_creative": {
                    if (message.isItemStackMessage()) {
                        UtilsInteractions.setDisableBlockBreakingInCreative(message.getItemStackValue(), true);
                    } else LOG.error("IMC@interactions.block_in_creative: value must be ItemStack.");
                }
                break;
                case "interactions.unblock_in_creative": {
                    if (message.isItemStackMessage()) {
                        UtilsInteractions.setDisableBlockBreakingInCreative(message.getItemStackValue(), false);
                    } else LOG.error("IMC@interactions.unblock_in_creative: value must be ItemStack.");
                }
                break;
                case "interactions.pigzombie_trigger_block": {
                    if (message.isItemStackMessage()) {
                        UtilsInteractions.setPigmanTriggerBlock(message.getItemStackValue(), true);
                    } else LOG.error("IMC@interactions.pigzombie_trigger_block: value must be ItemStack.");
                }
                break;
                case "interactions.not_pigzombie_trigger_block": {
                    if (message.isItemStackMessage()) {
                        UtilsInteractions.setPigmanTriggerBlock(message.getItemStackValue(), false);
                    } else LOG.error("IMC@interactions.not_pigzombie_trigger_block: value must be ItemStack.");
                }
                break;
                default:
                    LOG.warn(String.format("Unknown IMC message: %s", message.key));
            }
    }
}
