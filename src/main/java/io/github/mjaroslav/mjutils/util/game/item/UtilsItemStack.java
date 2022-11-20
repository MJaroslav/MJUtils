package io.github.mjaroslav.mjutils.util.game.item;

import io.github.mjaroslav.mjutils.util.UtilsFormat;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static net.minecraftforge.oredict.OreDictionary.*;

@UtilityClass
public class UtilsItemStack {
    /**
     * Returns true if items of stacks is equals.
     * Returns true if both stacks is null.
     *
     * @see UtilsItemStack#equals(ItemStack, ItemStack, int)
     */
    public final int ITEM = 0b000000001;
    /**
     * Returns true if items of stacks is equals.
     * Returns false if one of stacks is null.
     *
     * @see UtilsItemStack#equals(ItemStack, ItemStack, int)
     */
    public final int ITEM_STRONG = 0b000000010;
    /**
     * Returns true if sizes of stacks is equals.
     * Returns false if one of sizes less than 1.
     *
     * @see UtilsItemStack#equals(ItemStack, ItemStack, int)
     */
    public final int COUNT = 0b000000100;
    /**
     * Returns true if sizes of stacks is equals.
     * Does not preliminarily return false if one of sizes less than 1.
     *
     * @see UtilsItemStack#equals(ItemStack, ItemStack, int)
     */
    public final int COUNT_STRONG = 0b000001000;
    /**
     * Returns true if metas of stacks is equals.
     * Return true if one of metas is {@link OreDictionary#WILDCARD_VALUE}.
     *
     * @see UtilsItemStack#equals(ItemStack, ItemStack, int)
     */
    public final int META = 0b000010000;
    /**
     * Returns true if metas of stacks is equals.
     * Its ignores {@link OreDictionary#WILDCARD_VALUE}.
     *
     * @see UtilsItemStack#equals(ItemStack, ItemStack, int)
     */
    public final int META_STRONG = 0b000100000;
    /**
     * Returns true if NBT of stacks is equals.
     * Returns true if both is null.
     *
     * @see UtilsItemStack#equals(ItemStack, ItemStack, int)
     */
    public final int NBT = 0b001000000;
    /**
     * Returns true if NBT of stacks is equals.
     * Returns false if one of NBT is null.
     *
     * @see UtilsItemStack#equals(ItemStack, ItemStack, int)
     */
    public final int NBT_STRONG = 0b010000000;
    /**
     * Returns false if one of stacks is null.
     *
     * @see UtilsItemStack#equals(ItemStack, ItemStack, int)
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

    public boolean equalsItems(@Nullable ItemStack a, @Nullable ItemStack b) {
        return equals(a, b, ITEM | META);
    }

    public boolean equals(@Nullable ItemStack a, @Nullable ItemStack b) {
        return equals(a, b, ITEM | COUNT | META | NBT);
    }

    public boolean equals(@Nullable ItemStack a, @Nullable ItemStack b, int params) {
        if (params == 0) return true;
        if (a == null || b == null) return !UtilsFormat.isMaskAnd(params, STACK_STRONG) && a == b;
        boolean flagItem = true, flagCount = true, flagMeta = true, flagNBT = true;
        Item aItem = a.getItem(), bItem = b.getItem();
        if (UtilsFormat.isMaskAnd(params, ITEM_STRONG)) flagItem = aItem != null && aItem.equals(bItem);
        else if (UtilsFormat.isMaskAnd(params, ITEM)) flagItem = Objects.equals(aItem, bItem);
        int aSize = a.stackSize, bSize = b.stackSize;
        if (UtilsFormat.isMaskAnd(params, COUNT_STRONG)) flagCount = aSize == bSize;
        else if (UtilsFormat.isMaskAnd(params, COUNT)) flagCount = aSize == bSize && aSize > 0;
        int aMeta = a.getItemDamage(), bMeta = b.getItemDamage();
        if (UtilsFormat.isMaskAnd(params, META_STRONG)) flagMeta = aMeta == bMeta;
        else if (UtilsFormat.isMaskAnd(params, META))
            flagMeta = aMeta == bMeta || aMeta == OreDictionary.WILDCARD_VALUE || bMeta == OreDictionary.WILDCARD_VALUE;
        NBTTagCompound aNBT = a.getTagCompound(), bNBT = b.getTagCompound();
        if (UtilsFormat.isMaskAnd(params, NBT_STRONG)) flagNBT = aNBT != null && aNBT.equals(bNBT);
        else if (UtilsFormat.isMaskAnd(params, NBT)) flagNBT = Objects.equals(aNBT, bNBT);
        return flagItem && flagCount && flagMeta && flagNBT;
    }

    @Contract("null -> fail")
    public @NotNull ItemStack newStack(@Nullable Object object) {
        return newStack(object, 1, WILDCARD_VALUE);
    }

    @Contract("null, _ -> fail")
    public @NotNull ItemStack newStack(@Nullable Object object, int count) {
        return newStack(object, count, WILDCARD_VALUE);
    }

    @Contract("null, _, _ -> fail")
    public @NotNull ItemStack newStack(@Nullable Object object, int count, int damage) {
        if (object instanceof Block block) return new ItemStack(block, count, damage);
        else if (object instanceof ItemBlock item)
            return new ItemStack(Block.getBlockFromItem(item), count, damage);
        else if (object instanceof ItemStack stack) {
            val result = stack.copy();
            result.stackSize = count;
            result.setItemDamage(damage);
            return result;
        } else if (object instanceof String registryName) return new ItemStack(Block.getBlockFromName(registryName));
        else
            throw new IllegalArgumentException("Value must be Block, Item , ItemStack or String (with registry name)!");
    }

    public int hashCode(@Nullable ItemStack stack, int params) {
        if (stack == null) return 0;
        var result = 1;
        if (UtilsFormat.isMaskOr(params, STACK_STRONG | ITEM | ITEM_STRONG))
            result = 31 * result + (stack.getItem() == null ? 0 : Item.getIdFromItem(stack.getItem()));
        if (UtilsFormat.isMaskOr(params, STACK_STRONG | COUNT | COUNT_STRONG))
            result = 31 * result + (UtilsFormat.isMaskAnd(params, COUNT) && stack.stackSize < 1 ? 0 : stack.stackSize);
        if (UtilsFormat.isMaskOr(params, STACK_STRONG | META | META_STRONG))
            result = 31 * result + (UtilsFormat.isMaskAnd(params, META) && stack.getItemDamage() == WILDCARD_VALUE ? 0 : stack.getItemDamage());
        if (UtilsFormat.isMaskOr(params, STACK_STRONG | NBT | NBT_STRONG))
            result = 31 * result + (UtilsFormat.isMaskAnd(params, NBT) && !stack.hasTagCompound() ? 0 : stack.getTagCompound().hashCode());
        return result;
    }
}
