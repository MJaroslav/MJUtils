package mjaroslav.mcmods.example.mjutils;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mjaroslav.mcmods.mjutils.common.anvil.AnvilCraftingEvent;
import mjaroslav.mcmods.mjutils.common.fishing.FishingUtils;
import mjaroslav.mcmods.mjutils.common.fuel.FuelUtils;
import mjaroslav.mcmods.mjutils.common.objects.IModModule;
import mjaroslav.mcmods.mjutils.common.objects.ModInitModule;
import mjaroslav.mcmods.mjutils.common.reaction.AngryPigZombieByBlockBreaking;
import mjaroslav.mcmods.mjutils.common.reaction.ReactionUtils;
import mjaroslav.mcmods.mjutils.lib.MJInfo;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.FishingHooks.FishableCategory;
import net.minecraftforge.common.MinecraftForge;

@ModInitModule(modid = "mjutilsexample")
public class ExampleModule implements IModModule {
	@Override
	public String getModuleName() {
		return "ExampleBase";
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		FishingUtils.addItemToCategory(new ItemStack(Items.coal), MJInfo.fishingRarityJunk, FishableCategory.JUNK);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		FuelUtils.addFuel(new ItemStack(Items.wooden_door), 3F);
		ReactionUtils.addBlockToPigAngryList(Blocks.quartz_ore, MJInfo.anyMeta);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@SubscribeEvent
	public void onAnvilCrafting(AnvilCraftingEvent event) {
	}

	@SubscribeEvent
	public void onPigAngryByBlock(AngryPigZombieByBlockBreaking event) {

	}
}
