package io.github.mjaroslav.mjutils.util.item;

import io.github.mjaroslav.mjutils.util.object.DelegatingSet;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;

import static io.github.mjaroslav.mjutils.util.item.UtilsItemStack.*;

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
        super((stack, obj) -> UtilsItemStack.equals(stack, obj instanceof ItemStack second ? second : null, params),
            stack -> UtilsItemStack.hashCode(stack, params), new HashSet<>());
    }

    public ItemStackSet(@NotNull Collection<ItemStack> stacks, int params) {
        this(params);
        addAll(stacks);
    }
}
