package mjaroslav.mcmods.mjutils.common.init;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.GameRegistry;
import mjaroslav.mcmods.mjutils.common.anvil.*;
import mjaroslav.mcmods.mjutils.common.fuel.FuelHandler;
import mjaroslav.mcmods.mjutils.common.objects.IModModule;
import mjaroslav.mcmods.mjutils.common.objects.ModInitModule;
import mjaroslav.mcmods.mjutils.common.objects.ConfigurationBase.ConfigurationEventHandler;
import mjaroslav.mcmods.mjutils.common.reaction.ReactionEventHandler;
import mjaroslav.mcmods.mjutils.common.tooltip.TooltipEventHandler;
import mjaroslav.mcmods.mjutils.lib.ConfigCommonInfo;
import mjaroslav.mcmods.mjutils.lib.ModInfo;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

@ModInitModule(modid = ModInfo.MODID)
public class ModuleMain implements IModModule {
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
        if (ConfigCommonInfo.chainToIron) {
            UtilsAnvil.addRecipe(new ItemStack(Items.iron_helmet, 1),
                    new AnvilRecipe(new ItemStack(Items.chainmail_helmet, 1, 0), new ItemStack(Items.iron_ingot, 1)));
            UtilsAnvil.addRecipe(new ItemStack(Items.iron_chestplate, 1), new AnvilRecipe(
                    new ItemStack(Items.chainmail_chestplate, 1, 0), new ItemStack(Items.iron_ingot, 1)));
            UtilsAnvil.addRecipe(new ItemStack(Items.iron_leggings, 1),
                    new AnvilRecipe(new ItemStack(Items.chainmail_leggings, 1, 0), new ItemStack(Items.iron_ingot, 1)));
            UtilsAnvil.addRecipe(new ItemStack(Items.iron_boots, 1),
                    new AnvilRecipe(new ItemStack(Items.chainmail_boots, 1, 0), new ItemStack(Items.iron_ingot, 1)));
        }
        if (ConfigCommonInfo.leatherToChain) {
            UtilsAnvil.addRecipe(new ItemStack(Items.chainmail_helmet, 1),
                    new AnvilRecipe(new ItemStack(Items.leather_helmet, 1, 0), new ItemStack(Blocks.iron_bars, 2)));
            UtilsAnvil.addRecipe(new ItemStack(Items.chainmail_chestplate, 1),
                    new AnvilRecipe(new ItemStack(Items.leather_chestplate, 1, 0), new ItemStack(Blocks.iron_bars, 2)));
            UtilsAnvil.addRecipe(new ItemStack(Items.chainmail_leggings, 1),
                    new AnvilRecipe(new ItemStack(Items.leather_leggings, 1, 0), new ItemStack(Blocks.iron_bars, 2)));
            UtilsAnvil.addRecipe(new ItemStack(Items.chainmail_boots, 1),
                    new AnvilRecipe(new ItemStack(Items.leather_boots, 1, 0), new ItemStack(Blocks.iron_bars, 2)));
        }
        GameRegistry.registerFuelHandler(new FuelHandler());
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
