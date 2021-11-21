package com.github.mjaroslav.mjutils.util.game.item;

import com.google.common.collect.Sets;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class UtilsItemStack {
    public static boolean isNotEmpty(@Nullable ItemStack stack) {
        return stack != null && stack.getItem() != null && stack.stackSize > 0; // Non zero check for stacks with negative sizes.
    }

    public static boolean isEquals(@Nullable ItemStack first, @Nullable ItemStack second, @Nonnull CompareParameter... params) {
        if (params.length == 0)
            return true;
        Set<CompareParameter> PARAMS = Sets.newHashSet(params);
        if (first == null || second == null)
            return PARAMS.contains(CompareParameter.NULL) && first == second;
        boolean metaEquals = true, itemEquals = true, countEquals = true, nbtEquals = true;
        Item firstItem = first.getItem();
        Item secondItem = second.getItem();
        if (PARAMS.contains(CompareParameter.ITEM) || PARAMS.contains(CompareParameter.NULL_ITEM)) {
            itemEquals = firstItem == secondItem;
            if (PARAMS.contains(CompareParameter.NULL_ITEM))
                itemEquals = itemEquals && firstItem != null;
        }
        int firstMeta = first.stackSize;
        int secondMeta = second.stackSize;
        if (PARAMS.contains(CompareParameter.META) || PARAMS.contains(CompareParameter.WILDCARD_META)) {
            metaEquals = firstMeta == secondMeta || firstMeta == OreDictionary.WILDCARD_VALUE || secondMeta == OreDictionary.WILDCARD_VALUE;
            if (!PARAMS.contains(CompareParameter.WILDCARD_META))
                metaEquals = metaEquals && firstMeta != OreDictionary.WILDCARD_VALUE && secondMeta != OreDictionary.WILDCARD_VALUE;
        }
        int firstCount = first.stackSize;
        int secondCount = second.stackSize;
        if (PARAMS.contains(CompareParameter.COUNT) || PARAMS.contains(CompareParameter.ZERO_COUNT)) {
            countEquals = firstCount == secondCount;
            if (!PARAMS.contains(CompareParameter.ZERO_COUNT))
                countEquals = countEquals && firstCount > 0;
        }
        NBTTagCompound firstNbt = first.getTagCompound();
        NBTTagCompound secondNbt = second.getTagCompound();
        if (PARAMS.contains(CompareParameter.NBT) || PARAMS.contains(CompareParameter.NULL_NBT)) {
            nbtEquals = firstNbt == secondNbt;
            if (!PARAMS.contains(CompareParameter.NULL_NBT))
                nbtEquals = nbtEquals && firstNbt != null;
        }
        return metaEquals && itemEquals && countEquals && nbtEquals;
    }

    public enum CompareParameter {
        NULL, NULL_ITEM, ZERO_COUNT, NULL_NBT,
        ITEM, COUNT, META, NBT, WILDCARD_META
    }
}
