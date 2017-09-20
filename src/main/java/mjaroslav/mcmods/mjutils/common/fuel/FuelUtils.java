package mjaroslav.mcmods.mjutils.common.fuel;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.registry.GameRegistry;
import mjaroslav.mcmods.mjutils.MJInfo;
import net.minecraft.item.ItemStack;

public class FuelUtils {
	private static Map<ItemStack, Integer> fuelMap = new HashMap<>();

	public static Map<ItemStack, Integer> getFuelMap() {
		return fuelMap;
	}

	public static void addFuel(ItemStack itemStack, int burnTime) {
		fuelMap.put(itemStack, burnTime);
	}

	public static void addFuel(ItemStack itemStack, float burnCount) {
		fuelMap.put(itemStack, Math.round(MJInfo.ticksInSmelt * burnCount));
	}

	public static float getBurnCount(ItemStack fuel) {
		return GameRegistry.getFuelValue(fuel) / MJInfo.ticksInSmelt;
	}
}
