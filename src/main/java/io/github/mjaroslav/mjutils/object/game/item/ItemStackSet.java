package io.github.mjaroslav.mjutils.object.game.item;

import io.github.mjaroslav.mjutils.object.DelegatingSet;
import io.github.mjaroslav.mjutils.util.game.item.UtilsItemStack;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;

import static io.github.mjaroslav.mjutils.util.game.item.UtilsItemStack.*;

/**
 * Set for ItemStacks.
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
