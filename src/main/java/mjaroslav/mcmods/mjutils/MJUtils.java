package mjaroslav.mcmods.mjutils;

import java.util.ArrayList;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mjaroslav.mcmods.mjutils.common.anvil.AnvilModule;
import mjaroslav.mcmods.mjutils.common.init.IInitBase;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mod(modid = MJUtils.MODID, name = MJUtils.NAME, version = MJUtils.VERSION)
public class MJUtils {
	public static final String MODID = "mjutils";
	public static final String NAME = "MJUtils";
	public static final String VERSION = "1.7.10-1";

	public ArrayList<IInitBase> modules = new ArrayList<IInitBase>();

	public void initModules() {
		this.modules.add(new AnvilModule());
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		initModules();
		for (IInitBase module : this.modules)
			module.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		for (IInitBase module : this.modules)
			module.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		for (IInitBase module : this.modules)
			module.postInit(event);
	}

	public static boolean itemStackNotNull(ItemStack stack) {
		return stack != null ? stack.getItem() != null : false;
	}

	public static boolean itemStackNotNullAndEquals(ItemStack first, ItemStack second) {
		return first != null && second != null
				? first.getItem() != null && second.getItem() != null ? first.getItem() == second.getItem() : false
				: false;
	}

	public static boolean itemStackNotNullAndEquals(ItemStack first, Item second) {
		return first != null ? first.getItem() != null ? first.getItem() == second : false : false;
	}

	public static boolean itemStackNotNullAndEquals(ItemStack first, Block block) {
		return itemStackNotNullAndEquals(first, Item.getItemFromBlock(block));
	}
}
