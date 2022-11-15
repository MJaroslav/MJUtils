package io.github.mjaroslav.mjutils.object.game.item;

import io.github.mjaroslav.mjutils.object.DelegatingMap;
import io.github.mjaroslav.mjutils.util.game.item.UtilsItemStack;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

import static io.github.mjaroslav.mjutils.util.game.item.UtilsItemStack.*;

public class ItemStackMap<V> extends DelegatingMap<ItemStack, V> {
    public ItemStackMap() {
        this(ITEM | COUNT | META | NBT);
    }

    public ItemStackMap(int params) {
        super((a, b) -> UtilsItemStack.isEquals(a, (ItemStack) b, params), null, new HashMap<>());
    }
}
