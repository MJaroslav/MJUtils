package mjaroslav;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import mjaroslav.mcmods.fishingcontroller.api.FishingControllerApi;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.FishingHooks.FishableCategory;

@Mod(modid = "fishingcontrollertest", name = "Fishing Controller Test", version = "1.7.10-1")
public class TestMod {
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		FishingControllerApi.clearAllCategories(false);
		FishingControllerApi.addItemToCategory(new ItemStack(Items.beef), FishingControllerApi.rarityFish,
				FishableCategory.FISH);
		FishingControllerApi.addItemToCategory(new ItemStack(Items.arrow), FishingControllerApi.rarityJunk,
				FishableCategory.JUNK);
		FishingControllerApi.addItemToCategory(new ItemStack(Items.diamond), FishingControllerApi.rarityTreasure,
				FishableCategory.TREASURE);
	}
}
