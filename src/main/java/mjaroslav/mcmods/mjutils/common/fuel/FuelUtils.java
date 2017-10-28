package mjaroslav.mcmods.mjutils.common.fuel;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.registry.GameRegistry;
import mjaroslav.mcmods.mjutils.MJInfo;
import net.minecraft.item.ItemStack;

/**
 * Wrapper for fuel handler, here you can add your fuel and get the time of its
 * burning.
 * 
 * @author MJaroslav
 *
 */
public class FuelUtils {
	/**
	 * Fuel map, use {@link FuelUtils#getFuelMap()}.
	 */
	private static Map<ItemStack, Integer> fuelMap = new HashMap<>();

	/**
	 * @return Map of fuels and time of its burning.
	 */
	public static Map<ItemStack, Integer> getFuelMap() {
		return fuelMap;
	}

	/**
	 * Add new fuel.
	 * 
	 * @param itemStack
	 *            - fuel item.
	 * @param burnTime
	 *            - ticks of burn.
	 */
	public static void addFuel(ItemStack itemStack, int burnTime) {
		fuelMap.put(itemStack, burnTime);
	}

	/**
	 * Add new fuel.
	 * 
	 * @param itemStack
	 *            - fuel item.
	 * @param burnCount
	 *            - count of items to burn (1F = 200 ticks).
	 */
	public static void addFuel(ItemStack itemStack, float burnCount) {
		fuelMap.put(itemStack, Math.round(MJInfo.ticksInSmelt * burnCount));
	}

	/**
	 * @return The burning time of fuel in items (1F = 200 ticks).
	 */
	public static float getBurnCount(ItemStack fuel) {
		return GameRegistry.getFuelValue(fuel) / MJInfo.ticksInSmelt;
	}
}
