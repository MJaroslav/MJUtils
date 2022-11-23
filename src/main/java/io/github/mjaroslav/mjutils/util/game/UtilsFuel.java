package io.github.mjaroslav.mjutils.util.game;

import io.github.mjaroslav.mjutils.util.object.game.ItemStackMap;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Map;

@UtilityClass
public class UtilsFuel {
    @Getter
    private final Map<ItemStack, Integer> fuelMap = new ItemStackMap<>(UtilsItemStack.META | UtilsItemStack.ITEM);

    // Furnace store burn/cook time in short
    public void addFuel(@NotNull ItemStack itemStack, @Range(from = 0, to = Short.MAX_VALUE) int burnTime) {
        if (burnTime == 0) fuelMap.remove(itemStack);
        else fuelMap.put(itemStack, burnTime);
    }

    // Furnace store burn/cook time in short
    public void addFuel(@NotNull ItemStack itemStack, @Range(from = 0, to = 1638) float smeltingCount) {
        addFuel(itemStack, UtilsTime.getFurnaceSmeltingTicks(smeltingCount));
    }

    public int getFuelTicks(@NotNull ItemStack itemsTack) {
        return TileEntityFurnace.getItemBurnTime(itemsTack);
    }
}
