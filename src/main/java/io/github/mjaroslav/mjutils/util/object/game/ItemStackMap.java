package io.github.mjaroslav.mjutils.util.object.game;

import io.github.mjaroslav.mjutils.util.game.UtilsItemStack;
import io.github.mjaroslav.mjutils.util.object.DelegatingMap;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

import static io.github.mjaroslav.mjutils.util.game.UtilsItemStack.*;

/**
 * Special case of {@link DelegatingMap} for using {@link ItemStack} as keys. Uses params
 * from {@link UtilsItemStack#equals(ItemStack, ItemStack, int)}.
 *
 * @param <V> map value type.
 * @see ItemStack
 * @see DelegatingMap
 */
public class ItemStackMap<V> extends DelegatingMap<ItemStack, V> {
    public ItemStackMap() {
        this(ITEM | COUNT | META | NBT);
    }

    public ItemStackMap(int params) {
        super((stack, obj) -> UtilsItemStack.equals(stack, obj instanceof ItemStack second ? second : null, params),
            stack -> UtilsItemStack.hashCode(stack, params), new HashMap<>());
    }
}
