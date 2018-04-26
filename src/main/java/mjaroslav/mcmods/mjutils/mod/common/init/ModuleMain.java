package mjaroslav.mcmods.mjutils.mod.common.init;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.GameRegistry;
import mjaroslav.mcmods.mjutils.lib.handler.*;
import mjaroslav.mcmods.mjutils.lib.module.ConfigurationBase.ConfigurationEventHandler;
import mjaroslav.mcmods.mjutils.lib.module.IModule;
import mjaroslav.mcmods.mjutils.lib.module.ModModule;
import mjaroslav.mcmods.mjutils.lib.utils.UtilsAnvil;
import mjaroslav.mcmods.mjutils.mod.lib.ConfigCommonInfo;
import mjaroslav.mcmods.mjutils.mod.lib.ModInfo;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

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
        if (ConfigCommonInfo.goldenApples) {
            UtilsAnvil.addRecipe(new ItemStack(Items.golden_apple, 1),
                    new AnvilRecipe(new ItemStack(Items.apple, 1), new ItemStack(Items.gold_ingot, 6), 3));
            UtilsAnvil.addRecipe(new ItemStack(Items.golden_apple, 1, 1),
                    new AnvilRecipe(new ItemStack(Items.apple, 1), new ItemStack(Blocks.gold_block, 6), 6));
            UtilsAnvil.addRecipe(new ItemStack(Items.golden_apple, 1, 1),
                    new AnvilRecipe(new ItemStack(Items.golden_apple, 1), new ItemStack(Blocks.gold_block, 4), 8));
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
