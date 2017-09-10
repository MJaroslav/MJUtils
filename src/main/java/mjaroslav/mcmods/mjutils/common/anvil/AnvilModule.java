package mjaroslav.mcmods.mjutils.common.anvil;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mjaroslav.mcmods.mjutils.common.anvil.AnvilUtils.AnvilResult;
import mjaroslav.mcmods.mjutils.common.init.IInitBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

public class AnvilModule implements IInitBase {
	@Override
	public void preInit(FMLPreInitializationEvent event) {

	}

	@Override
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new AnvilEvent());
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		AnvilUtils.instance().addRecipe(new AnvilResult(new ItemStack(Items.golden_apple, 2, 1), 15),
				new AnvilRecipe(new ItemStack(Items.apple, 1), new ItemStack(Blocks.gold_block, 1)).setName("upgrade"));
		AnvilUtils.instance().addRecipe(new AnvilResult(new ItemStack(Blocks.carpet, 8, 4), 2),
				new AnvilRecipe(new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE),
						new ItemStack(Items.dye, 1, 4)).setRightName("painter"));
		AnvilUtils.instance().addRecipe(new AnvilResult(new ItemStack(Blocks.planks, 12), 0),
				new AnvilRecipe(new ItemStack(Blocks.log, 1), new ItemStack(Blocks.log, 3)).setLeftName("thelog").setName("compressing"));
	}

}
