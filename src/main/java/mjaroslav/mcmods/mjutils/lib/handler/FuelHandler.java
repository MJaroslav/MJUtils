package mjaroslav.mcmods.mjutils.lib.handler;

import java.util.Map.Entry;

import cpw.mods.fml.common.IFuelHandler;
import mjaroslav.mcmods.mjutils.lib.utils.UtilsFuel;
import mjaroslav.mcmods.mjutils.lib.utils.UtilsGame;
import net.minecraft.item.ItemStack;

public class FuelHandler implements IFuelHandler {
    @Override
    public int getBurnTime(ItemStack fuel) {
        for (Entry<ItemStack, Integer> entry : UtilsFuel.getFuelMap().entrySet())
            if (UtilsGame.itemStacksEquals(fuel, entry.getKey()))
                return entry.getValue();
        return 0;
    }
}
