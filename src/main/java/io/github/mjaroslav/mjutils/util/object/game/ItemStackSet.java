package io.github.mjaroslav.mjutils.util.object.game;

import io.github.mjaroslav.mjutils.util.game.UtilsItemStack;
import io.github.mjaroslav.sharedjava.util.DelegatingSet;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static io.github.mjaroslav.mjutils.util.game.UtilsItemStack.*;

/**
 * Special case of {@link DelegatingSet} for using {@link ItemStack}. Uses params
 * from {@link UtilsItemStack#equals(ItemStack, ItemStack, int)}.
 *
 * @see ItemStack
 * @see DelegatingSet
 */
public class ItemStackSet extends DelegatingSet<ItemStack> {
    public ItemStackSet() {
        this(ITEM | COUNT | META | NBT);
    }

    public ItemStackSet(int params) {
        super((unit, objUnit) -> UtilsItemStack.equals(unit.getX(),
                objUnit != null && objUnit.getX() instanceof ItemStack second ? second : null, params),
            unit -> UtilsItemStack.hashCode(unit.getX(), params), null);
    }

    public ItemStackSet(@NotNull Collection<ItemStack> stacks, int params) {
        this(params);
        addAll(stacks);
    }
}
