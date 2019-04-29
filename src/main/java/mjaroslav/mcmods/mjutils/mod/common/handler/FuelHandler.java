package mjaroslav.mcmods.mjutils.mod.common.handler;

import cpw.mods.fml.common.IFuelHandler;
import mjaroslav.mcmods.mjutils.util.UtilsFuel;
import mjaroslav.mcmods.mjutils.util.UtilsInventory;
import net.minecraft.item.ItemStack;

import java.util.Map.Entry;

public class FuelHandler implements IFuelHandler {
    public static final FuelHandler instance = new FuelHandler();

    private FuelHandler() {
    }

    @Override
    public int getBurnTime(ItemStack fuel) {
        for (Entry<ItemStack, Integer> entry : UtilsFuel.getFuelMap().entrySet())
            if (UtilsInventory.itemStackTypeEquals(fuel, entry.getKey(), true))
                return entry.getValue();
        return 0;
    }
}
