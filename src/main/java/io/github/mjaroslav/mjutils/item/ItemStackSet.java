package io.github.mjaroslav.mjutils.item;

import io.github.mjaroslav.sharedjava.util.DelegatingSet;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static io.github.mjaroslav.mjutils.item.Stacks.*;

public class ItemStackSet extends DelegatingSet<ItemStack> {
    public ItemStackSet() {
        this(ITEM_NULLABLE | COUNT_NONE | META_WILDCARD | NBT_NULLABLE);
    }

    public ItemStackSet(int params) {
        super((unit, objUnit) -> Stacks.equals(unit.getX(),
                objUnit != null && objUnit.getX() instanceof ItemStack second ? second : null, params),
            unit -> Stacks.hashCode(unit.getX(), params), null);
    }

    public ItemStackSet(@NotNull Collection<ItemStack> stacks) {
        this();
        addAll(stacks);
    }

    public ItemStackSet(@NotNull Collection<ItemStack> stacks, int params) {
        this(params);
        addAll(stacks);
    }
}
