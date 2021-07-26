package com.github.mjaroslav.mjutils.mod.common.handler;

import com.github.mjaroslav.mjutils.util.UtilsFuel;
import com.github.mjaroslav.mjutils.util.common.UtilsItemStack;
import com.github.mjaroslav.mjutils.util.common.UtilsItemStack.CompareParameter;
import cpw.mods.fml.common.IFuelHandler;
import net.minecraft.item.ItemStack;

import java.util.Map.Entry;

public class FuelHandler implements IFuelHandler {
    public static final FuelHandler instance = new FuelHandler();

    private FuelHandler() {
    }

    @Override
    public int getBurnTime(ItemStack fuel) {
        for (Entry<ItemStack, Integer> entry : UtilsFuel.getFuelMap().entrySet())
            if (UtilsItemStack.isEquals(fuel, entry.getKey(), CompareParameter.ITEM, CompareParameter.META))
                return entry.getValue();
        return 0;
    }
}