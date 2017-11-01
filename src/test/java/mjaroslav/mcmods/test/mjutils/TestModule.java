package mjaroslav.mcmods.test.mjutils;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mjaroslav.mcmods.mjutils.common.anvil.AnvilRecipe;
import mjaroslav.mcmods.mjutils.common.anvil.AnvilUtils;
import mjaroslav.mcmods.mjutils.common.anvil.AnvilUtils.AnvilResult;
import mjaroslav.mcmods.mjutils.common.fishing.FishingUtils;
import mjaroslav.mcmods.mjutils.common.fuel.FuelUtils;
import mjaroslav.mcmods.mjutils.common.objects.IModModule;
import mjaroslav.mcmods.mjutils.common.objects.ModInitModule;
import mjaroslav.mcmods.mjutils.lib.MJInfo;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.FishingHooks.FishableCategory;

@ModInitModule(modid = "mjutilstest")
public class TestModule implements IModModule {
	@Override
	public String getModuleName() {
		return "TestBase";
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		FishingUtils.addItemToCategory(new ItemStack(Items.coal), MJInfo.fishingRarityJunk, FishableCategory.JUNK);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		FuelUtils.addFuel(new ItemStack(Items.wooden_door), 3F);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
	}
	
	@Override
	public int getPriority() {
		return 0;
	}
}
