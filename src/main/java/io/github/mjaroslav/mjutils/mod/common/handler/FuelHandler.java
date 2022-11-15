package io.github.mjaroslav.mjutils.mod.common.handler;

import cpw.mods.fml.common.IFuelHandler;
import io.github.mjaroslav.mjutils.util.game.UtilsFuel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FuelHandler implements IFuelHandler {
    public static final FuelHandler INSTANCE = new FuelHandler();

    @Override
    public int getBurnTime(@NotNull ItemStack fuel) {
        return UtilsFuel.getFuelMap().getOrDefault(fuel, 0);
    }
}
