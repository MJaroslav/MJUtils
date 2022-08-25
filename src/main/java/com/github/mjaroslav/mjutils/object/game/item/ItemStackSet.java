package com.github.mjaroslav.mjutils.object.game.item;

import com.github.mjaroslav.mjutils.object.DelegatingSet;
import com.github.mjaroslav.mjutils.util.game.item.UtilsItemStack;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;

/**
 * Set for ItemStacks.
 */
public class ItemStackSet extends DelegatingSet<ItemStack> {
    public ItemStackSet(int params) {
        super((stack, obj) -> {
            ItemStack secondStack = null;
            if (obj instanceof ItemStack)
                secondStack = (ItemStack) obj;
            return UtilsItemStack.isEquals(stack, secondStack, params);
        }, null, new HashSet<>());
    }

    public ItemStackSet(@NotNull Collection<ItemStack> stacks, int params) {
        this(params);
        addAll(stacks);
    }
}
