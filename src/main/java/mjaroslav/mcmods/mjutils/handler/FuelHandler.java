package mjaroslav.mcmods.mjutils.handler;

import java.util.Map.Entry;

import cpw.mods.fml.common.IFuelHandler;
import mjaroslav.mcmods.mjutils.util.UtilsFuel;
import mjaroslav.mcmods.mjutils.util.UtilsInventory;
import net.minecraft.item.ItemStack;

public class FuelHandler implements IFuelHandler {
    @Override
    public int getBurnTime(ItemStack fuel) {
        for (Entry<ItemStack, Integer> entry : UtilsFuel.getFuelMap().entrySet())
            if (UtilsInventory.itemStackTypeEquals(fuel, entry.getKey(), true))
                return entry.getValue();
        return 0;
    }
}
