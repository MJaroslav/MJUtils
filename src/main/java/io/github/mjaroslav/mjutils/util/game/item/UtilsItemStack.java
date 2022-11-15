package io.github.mjaroslav.mjutils.util.game.item;

import io.github.mjaroslav.mjutils.util.UtilsFormat;
import lombok.experimental.UtilityClass;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@UtilityClass
public class UtilsItemStack {
    /**
     * Returns true if items of stacks is equals.
     * Returns true if both stacks is null.
     *
     * @see UtilsItemStack#isEquals(ItemStack, ItemStack, int)
     */
    public final int ITEM = 0b000000001;
    /**
     * Returns true if items of stacks is equals.
     * Returns false if one of stacks is null.
     *
     * @see UtilsItemStack#isEquals(ItemStack, ItemStack, int)
     */
    public final int ITEM_STRONG = 0b000000010;
    /**
     * Returns true if sizes of stacks is equals.
     * Returns false if one of sizes less than 1.
     *
     * @see UtilsItemStack#isEquals(ItemStack, ItemStack, int)
     */
    public final int COUNT = 0b000000100;
    /**
     * Returns true if sizes of stacks is equals.
     * Does not preliminarily return false if one of sizes less than 1.
     *
     * @see UtilsItemStack#isEquals(ItemStack, ItemStack, int)
     */
    public final int COUNT_STRONG = 0b000001000;
    /**
     * Returns true if metas of stacks is equals.
     * Return true if one of metas is {@link OreDictionary#WILDCARD_VALUE}.
     *
     * @see UtilsItemStack#isEquals(ItemStack, ItemStack, int)
     */
    public final int META = 0b000010000;
    /**
     * Returns true if metas of stacks is equals.
     * Its ignores {@link OreDictionary#WILDCARD_VALUE}.
     *
     * @see UtilsItemStack#isEquals(ItemStack, ItemStack, int)
     */
    public final int META_STRONG = 0b000100000;
    /**
     * Returns true if NBT of stacks is equals.
     * Returns true if both is null.
     *
     * @see UtilsItemStack#isEquals(ItemStack, ItemStack, int)
     */
    public final int NBT = 0b001000000;
    /**
     * Returns true if NBT of stacks is equals.
     * Returns false if one of NBT is null.
     *
     * @see UtilsItemStack#isEquals(ItemStack, ItemStack, int)
     */
    public final int NBT_STRONG = 0b010000000;
    /**
     * Returns false if one of stacks is null.
     *
     * @see UtilsItemStack#isEquals(ItemStack, ItemStack, int)
     */
    public final int STACK_STRONG = 0b100000000;

    public @NotNull ItemStack requireNonNullStackOrElse(@Nullable ItemStack stack, @NotNull ItemStack defaultStack) {
        return isEmpty(stack) ? defaultStack : stack;
    }

    public @NotNull ItemStack requireNonNullStack(@Nullable ItemStack stack) {
        return isEmpty(stack) ? new ItemStack(Blocks.air, 1) : stack;
    }

    @Contract("null -> true")
    public boolean isEmpty(@Nullable ItemStack stack) {
        return stack == null || stack.getItem() == null || stack.stackSize < 1;
    }

    @Contract("null -> false")
    public boolean isNotEmpty(@Nullable ItemStack stack) {
        return stack != null && stack.getItem() != null && stack.stackSize > 0;
    }

    public boolean isItemsEquals(@Nullable ItemStack a, @Nullable ItemStack b) {
        return isEquals(a, b, ITEM | META);
    }

    public boolean isEquals(@Nullable ItemStack a, @Nullable ItemStack b) {
        return isEquals(a, b, ITEM | COUNT | META | NBT);
    }

    public boolean isEquals(@Nullable ItemStack a, @Nullable ItemStack b, int params) {
        if (params == 0)
            return true;
        if (a == null || b == null)
            return !UtilsFormat.isMask(params, STACK_STRONG) && a == b;
        boolean flagItem = true, flagCount = true, flagMeta = true, flagNBT = true;
        Item aItem = a.getItem(), bItem = b.getItem();
        if (UtilsFormat.isMask(params, ITEM_STRONG))
            flagItem = aItem != null && aItem.equals(bItem);
        else if (UtilsFormat.isMask(params, ITEM))
            flagItem = Objects.equals(aItem, bItem);
        int aSize = a.stackSize, bSize = b.stackSize;
        if (UtilsFormat.isMask(params, COUNT_STRONG))
            flagCount = aSize == bSize;
        else if (UtilsFormat.isMask(params, COUNT))
            flagCount = aSize == bSize && aSize > 0;
        int aMeta = a.getItemDamage(), bMeta = b.getItemDamage();
        if (UtilsFormat.isMask(params, META_STRONG))
            flagMeta = aMeta == bMeta;
        else if (UtilsFormat.isMask(params, META))
            flagMeta = aMeta == bMeta || aMeta == OreDictionary.WILDCARD_VALUE
                    || bMeta == OreDictionary.WILDCARD_VALUE;
        NBTTagCompound aNBT = a.getTagCompound(), bNBT = b.getTagCompound();
        if (UtilsFormat.isMask(params, NBT_STRONG))
            flagNBT = aNBT != null && aNBT.equals(bNBT);
        else if (UtilsFormat.isMask(params, NBT))
            flagNBT = Objects.equals(aNBT, bNBT);
        return flagItem && flagCount && flagMeta && flagNBT;
    }
}
