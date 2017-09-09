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
		AnvilUtils.instance().addRecipe(new AnvilResult(new ItemStack(Items.golden_apple), 15),
				new AnvilRecipe(new ItemStack(Items.apple), new ItemStack(Blocks.gold_block)));
		AnvilUtils.instance().addRecipe(new AnvilResult(new ItemStack(Items.cooked_beef), 10),
				new AnvilRecipe(new ItemStack(Items.beef), new ItemStack(Items.coal)).setLeftName("supermeat"));
		AnvilUtils.instance().addRecipe(new AnvilResult(new ItemStack(Items.cooked_porkchop), 5),
				new AnvilRecipe(new ItemStack(Items.coal), new ItemStack(Items.porkchop)).setRightName("superpork")
						.setName("cooking"));
	}

}
