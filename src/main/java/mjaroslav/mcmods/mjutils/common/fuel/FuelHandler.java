package mjaroslav.mcmods.mjutils.common.fuel;

import java.util.Map.Entry;

import cpw.mods.fml.common.IFuelHandler;
import mjaroslav.mcmods.mjutils.common.utils.UtilsGame;
import net.minecraft.item.ItemStack;

/**
 * Fuel handler for FuelUtils.
 *
 * @author MJaroslav
 */
public class FuelHandler implements IFuelHandler {
    @Override
    public int getBurnTime(ItemStack fuel) {
        for (Entry<ItemStack, Integer> entry : UtilsFuel.getFuelMap().entrySet()) {
            if (UtilsGame.itemStacksEquals(fuel, entry.getKey()))
                return entry.getValue();
        }
        return 0;
    }
}
