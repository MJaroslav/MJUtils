package io.github.mjaroslav.mjutils.object.game.item;

import io.github.mjaroslav.mjutils.util.game.item.UtilsItemStack;
import io.github.mjaroslav.mjutils.util.object.DelegatingMap;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

import static io.github.mjaroslav.mjutils.util.game.item.UtilsItemStack.*;

public class ItemStackMap<V> extends DelegatingMap<ItemStack, V> {
    public ItemStackMap() {
        this(ITEM | COUNT | META | NBT);
    }

    public ItemStackMap(int params) {
        super((stack, obj) -> UtilsItemStack.equals(stack, obj instanceof ItemStack second ? second : null, params),
            stack -> UtilsItemStack.hashCode(stack, params), new HashMap<>());
    }
}
