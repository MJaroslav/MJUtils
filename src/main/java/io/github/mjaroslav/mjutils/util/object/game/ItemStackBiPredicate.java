package io.github.mjaroslav.mjutils.util.object.game;

import io.github.mjaroslav.mjutils.util.game.UtilsItemStack;
import lombok.RequiredArgsConstructor;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiPredicate;

/**
 * BiPredicate for two ItemStack. This implementation assumes that a second argument is always ItemStack.
 * Uses params from {@link UtilsItemStack#equals(ItemStack, ItemStack, int)}.
 *
 * @see UtilsItemStack#equals(ItemStack, ItemStack, int)
 */
@RequiredArgsConstructor
public class ItemStackBiPredicate implements BiPredicate<ItemStack, ItemStack> {
    /**
     * Params from {@link UtilsItemStack#equals(ItemStack, ItemStack, int)}.
     */
    public final int params;

    @Override
    public boolean test(@Nullable ItemStack first, @Nullable ItemStack second) {
        return UtilsItemStack.equals(first, second, params);
    }
}
