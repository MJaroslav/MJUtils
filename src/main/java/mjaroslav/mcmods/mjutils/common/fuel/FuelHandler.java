package mjaroslav.mcmods.mjutils.common.fuel;

import java.util.Map.Entry;

import cpw.mods.fml.common.IFuelHandler;
import mjaroslav.mcmods.mjutils.common.utils.GameUtils;
import net.minecraft.item.ItemStack;

/**
 * 
 * @author MJaroslav
 *
 */
public class FuelHandler implements IFuelHandler {
	@Override
	public int getBurnTime(ItemStack fuel) {
		for (Entry<ItemStack, Integer> entry : FuelUtils.getFuelMap().entrySet()) {
			if (GameUtils.itemStacksEquals(fuel, entry.getKey()))
				return entry.getValue();
		}
		return 0;
	}
}
