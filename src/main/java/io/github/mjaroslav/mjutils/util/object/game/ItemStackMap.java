package io.github.mjaroslav.mjutils.util.object.game;

import io.github.mjaroslav.mjutils.util.game.UtilsItemStack;
import io.github.mjaroslav.sharedjava.util.DelegatingMap;
import net.minecraft.item.ItemStack;

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
        super((unit, objUnit) -> UtilsItemStack.equals(unit.getX(),
                objUnit != null && objUnit.getX() instanceof ItemStack second ? second : null, params),
            unit -> UtilsItemStack.hashCode(unit.getX(), params), null);
    }
}
