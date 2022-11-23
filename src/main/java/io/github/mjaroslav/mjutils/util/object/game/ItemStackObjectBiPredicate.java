package io.github.mjaroslav.mjutils.util.object.game;

import io.github.mjaroslav.mjutils.util.game.UtilsItemStack;
import lombok.RequiredArgsConstructor;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiPredicate;

/**
 * BiPredicate for ItemStack and any object. Uses params
 * from {@link UtilsItemStack#equals(ItemStack, ItemStack, int)}.
 *
 * @see UtilsItemStack#equals(ItemStack, ItemStack, int)
 */
@RequiredArgsConstructor
public class ItemStackObjectBiPredicate implements BiPredicate<ItemStack, Object> {
    /**
     * Params from {@link UtilsItemStack#equals(ItemStack, ItemStack, int)}.
     */
    public final int params;

    @Override
    public boolean test(@Nullable ItemStack firstStack, @Nullable Object second) {
        return second instanceof ItemStack secondStack && UtilsItemStack.equals(firstStack, secondStack, params);
    }
}
