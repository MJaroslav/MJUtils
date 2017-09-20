package mjaroslav.mcmods.mjutils.common.fuel;

import java.util.Iterator;
import java.util.Map.Entry;

import cpw.mods.fml.common.IFuelHandler;
import mjaroslav.mcmods.mjutils.common.utils.GameUtils;
import net.minecraft.item.ItemStack;

public class FuelHandler implements IFuelHandler {
	@Override
	public int getBurnTime(ItemStack fuel) {
		Iterator iterator = FuelUtils.getFuelMap().entrySet().iterator();
		Entry entry;
		do {
			if (!iterator.hasNext()) {
				return 0;
			}
			entry = (Entry) iterator.next();
		} while (!GameUtils.itemStacksEquals(fuel, (ItemStack) entry.getKey()));
		return (Integer) entry.getValue();
	}
}
