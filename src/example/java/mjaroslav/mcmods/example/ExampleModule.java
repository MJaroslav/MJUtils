package mjaroslav.mcmods.example;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mjaroslav.mcmods.mjutils.common.anvil.AnvilRecipe;
import mjaroslav.mcmods.mjutils.common.anvil.AnvilUtils;
import mjaroslav.mcmods.mjutils.common.fishing.FishingConstants;
import mjaroslav.mcmods.mjutils.common.fishing.FishingUtils;
import mjaroslav.mcmods.mjutils.common.objects.IModModule;
import mjaroslav.mcmods.mjutils.common.objects.ModInitModule;
import mjaroslav.mcmods.mjutils.common.reaction.ReactionUtils;
import mjaroslav.mcmods.mjutils.common.thaum.ResearchItemCopy;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.FishingHooks.FishableCategory;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.common.config.ConfigItems;

@ModInitModule(modid = ExampleInfo.MODID)
public class ExampleModule implements IModModule {
	@Override
	public String getModuleName() {
		return "ExampleBase";
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
	}

	@Override
	public void init(FMLInitializationEvent event) {
		// Register recipe for golden apple from apple and gold ingot (6), cost
		// 2 lvls.
		AnvilUtils.addRecipe(new ItemStack(Items.golden_apple, 1),
				new AnvilRecipe(new ItemStack(Items.apple, 1), new ItemStack(Items.gold_ingot, 6), 2));
		// Register recipe for nether star from diamond block with name 'core'
		// (any case and spaces) and wither skeleton skull (4), cost 30 lvls.
		AnvilUtils.addRecipe(new ItemStack(Items.nether_star, 1),
				new AnvilRecipe(new ItemStack(Blocks.diamond_block, 1), new ItemStack(Items.skull, 4, 1), "core", "",
						"netherstar", 30));
		// Register compass to junk type of fishing catch with default rarity.
		FishingUtils.addItemToCategory(new ItemStack(Items.compass, 1), FishingConstants.rarityJunk,
				FishableCategory.JUNK);
		// Register quartz ore in pig zombie 'angry blocks'.
		ReactionUtils.addBlockToPigAngryList(Blocks.quartz_ore, OreDictionary.WILDCARD_VALUE);
		FMLCommonHandler.instance().bus().register(new ExampleEventHandler());
	}

	public static ResearchItemCopy copyPureIron; // DO NOT INITIALIZE STATIC
													// OBJECTS OF THE GAME IN
													// THE MODULE, USE
													// INITIALIZATION METHODS!

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		// Register research copy item.
		copyPureIron = new ResearchItemCopy("PUREIRON", "COPYPUREIRON", "ALCHEMY", -5, 5,
				new ItemStack(ConfigItems.itemNugget, 1, 16)).registerResearchItem();
	}

	@Override
	public int getPriority() {
		return 0;
	}
}
