package io.github.mjaroslav.mjutils.item;

import io.github.mjaroslav.sharedjava.util.DelegatingMap;
import net.minecraft.item.ItemStack;

import static io.github.mjaroslav.mjutils.item.Stacks.*;

public class ItemStackMap<V> extends DelegatingMap<ItemStack, V> {
    public ItemStackMap() {
        this(ITEM_NULLABLE | COUNT_NONE | META_WILDCARD | NBT_NULLABLE);
    }

    public ItemStackMap(int params) {
        super((unit, objUnit) -> Stacks.equals(unit.getX(),
                objUnit != null && objUnit.getX() instanceof ItemStack second ? second : null, params),
            unit -> Stacks.hashCode(unit.getX(), params), null);
    }
}
