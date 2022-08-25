package com.github.mjaroslav.mjutils.mod.common.handler;

import com.github.mjaroslav.mjutils.util.game.UtilsFuel;
import cpw.mods.fml.common.IFuelHandler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map.Entry;

import static com.github.mjaroslav.mjutils.util.game.item.UtilsItemStack.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FuelHandler implements IFuelHandler {
    public static final FuelHandler instance = new FuelHandler();

    @Override
    public int getBurnTime(@NotNull ItemStack fuel) {
        return UtilsFuel.getFuelMap().entrySet().stream().filter(entry -> isEquals(fuel, entry.getKey(), META | ITEM))
                .findFirst().map(Entry::getValue).orElse(0);
    }
}
