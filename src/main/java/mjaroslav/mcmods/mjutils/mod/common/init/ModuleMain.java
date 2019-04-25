package mjaroslav.mcmods.mjutils.mod.common.init;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import mjaroslav.mcmods.mjutils.lib.event.FishingSuccessEvent;
import mjaroslav.mcmods.mjutils.lib.handler.*;
import mjaroslav.mcmods.mjutils.lib.module.ConfigurationBase.ConfigurationEventHandler;
import mjaroslav.mcmods.mjutils.lib.module.IModule;
import mjaroslav.mcmods.mjutils.lib.module.ModModule;
import mjaroslav.mcmods.mjutils.lib.util.UtilsGame;
import mjaroslav.mcmods.mjutils.lib.util.UtilsInteractions;
import mjaroslav.mcmods.mjutils.lib.util.UtilsInventory;
import mjaroslav.mcmods.mjutils.mod.lib.ModInfo;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

@ModModule(modid = ModInfo.MODID)
public class ModuleMain implements IModule {
    @Override
    public String getModuleName() {
        return "Main";
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
    }

    @Override
    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new ConfigurationEventHandler());
        MinecraftForge.EVENT_BUS.register(new AnvilEventHandler());
        MinecraftForge.EVENT_BUS.register(new ReactionEventHandler());
        MinecraftForge.EVENT_BUS.register(new TooltipEventHandler());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        GameRegistry.registerFuelHandler(new FuelHandler());
        UtilsInteractions.setDisableBlockBreakingInCreative(Items.iron_axe, true);
    }

    @Override
    public String[] modDependencies() {
        return null;
    }

    @Override
    public boolean canLoad() {
        return true;
    }
}
