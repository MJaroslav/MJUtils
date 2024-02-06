package io.github.mjaroslav.mjutils.util.game;

import io.github.mjaroslav.sharedjava.format.Bits;
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

/**
 * Utilities for working with {@link ItemStack ItemStacks}.
 */
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

    /**
     * Returns first argument if it is not empty else second.
     *
     * @param stack        nullable primary stack.
     * @param defaultStack non-null stack for using when first is null.
     * @return first if it is not empty or second stack else
     */
    public @NotNull ItemStack requireNonNullStackOrElse(@Nullable ItemStack stack, @NotNull ItemStack defaultStack) {
        return isEmpty(stack) ? defaultStack : stack;
    }

    /**
     * Return always non-empty stack by replacing empty value by stack with 1 air block.
     *
     * @param stack nullable stack for returning.
     * @return stack if it is no empty stack with 1 air else.
     */
    public @NotNull ItemStack requireNoEmptyStack(@Nullable ItemStack stack) {
        return isEmpty(stack) ? new ItemStack(Blocks.air, 1) : stack;
    }

    /**
     * Checks stack for null or empty.
     *
     * @param stack stack for check.
     * @return true if stack is null or stack item is null or stack size less than 1.
     */
    @Contract("null -> true")
    public boolean isEmpty(@Nullable ItemStack stack) {
        return stack == null || stack.getItem() == null || stack.stackSize < 1;
    }

    /**
     * Checks stack for not null and not empty.
     *
     * @param stack stack for check.
     * @return true if stack is not null and stack item is also not null and stack size more than 0.
     */
    @Contract("null -> false")
    public boolean isNotEmpty(@Nullable ItemStack stack) {
        return stack != null && stack.getItem() != null && stack.stackSize > 0;
    }

    /**
     * Checks two stacks for equals by item and meta. Equivalent of
     * {@code UtilsItemStack#equals(a, b, ITEM | META)}.
     *
     * @param a first nullable stack.
     * @param b second nullable stack.
     * @return true if items and metas of stack equals or both stacks is null.
     */
    public boolean equalsItems(@Nullable ItemStack a, @Nullable ItemStack b) {
        return equals(a, b, ITEM | META);
    }

    /**
     * Checks two stacks for non-strong equality.Equivalent of
     * {@code UtilsItemStack#equals(a, b, ITEM | COUNT | META | NBT)}.
     *
     * @param a first nullable stack.
     * @param b second nullable stack.
     * @return true if items, count, metas and nbt equals or both stacks is null. Count can be less than 1 for any match,
     * meta can be {@link OreDictionary#WILDCARD_VALUE} for any match and NBT can be null.
     */
    public boolean equals(@Nullable ItemStack a, @Nullable ItemStack b) {
        return equals(a, b, ITEM | COUNT | META | NBT);
    }

    /**
     * Checks two stacks for equality with specified parameters.
     *
     * @param a      first nullable stack.
     * @param b      second nullable stack.
     * @param params bit mask by OR-ing constant parameters: {@link UtilsItemStack#ITEM},
     *               {@link UtilsItemStack#ITEM_STRONG}, {@link UtilsItemStack#COUNT}, {@link UtilsItemStack#COUNT_STRONG},
     *               {@link UtilsItemStack#NBT}, {@link UtilsItemStack#NBT_STRONG}, {@link UtilsItemStack#META},
     *               {@link UtilsItemStack#META_STRONG} and {@link UtilsItemStack#STACK_STRONG}.
     * @return true if stack are equals by this specified parameters and true if no parameters passed.
     */
    public boolean equals(@Nullable ItemStack a, @Nullable ItemStack b, int params) {
        if (params == 0) return true;
        if (a == null || b == null) return !Bits.isMaskAnd(params, STACK_STRONG) && a == b;
        boolean flagItem = true, flagCount = true, flagMeta = true, flagNBT = true;
        Item aItem = a.getItem(), bItem = b.getItem();
        if (Bits.isMaskAnd(params, ITEM_STRONG)) flagItem = aItem != null && aItem.equals(bItem);
        else if (Bits.isMaskAnd(params, ITEM)) flagItem = Objects.equals(aItem, bItem);
        int aSize = a.stackSize, bSize = b.stackSize;
        if (Bits.isMaskAnd(params, COUNT_STRONG)) flagCount = aSize == bSize;
        else if (Bits.isMaskAnd(params, COUNT)) flagCount = aSize == bSize && aSize > 0;
        int aMeta = a.getItemDamage(), bMeta = b.getItemDamage();
        if (Bits.isMaskAnd(params, META_STRONG)) flagMeta = aMeta == bMeta;
        else if (Bits.isMaskAnd(params, META))
            flagMeta = aMeta == bMeta || aMeta == OreDictionary.WILDCARD_VALUE || bMeta == OreDictionary.WILDCARD_VALUE;
        NBTTagCompound aNBT = a.getTagCompound(), bNBT = b.getTagCompound();
        if (Bits.isMaskAnd(params, NBT_STRONG)) flagNBT = aNBT != null && aNBT.equals(bNBT);
        else if (Bits.isMaskAnd(params, NBT)) flagNBT = Objects.equals(aNBT, bNBT);
        return flagItem && flagCount && flagMeta && flagNBT;
    }

    /**
     * Create new 1 sized stack from Item, Block or registry name String with {@link OreDictionary#WILDCARD_VALUE}.
     *
     * @param object item, must be Item, Block or registry name.
     * @return new created item stack or fail if it can't be created.
     */
    @Contract("null -> fail")
    public @NotNull ItemStack newStack(@Nullable Object object) {
        return newStack(object, 1, WILDCARD_VALUE);
    }

    /**
     * Create new specific sized stack from Item, Block or registry name String with {@link OreDictionary#WILDCARD_VALUE}.
     *
     * @param object item, must be Item, Block or registry name.
     * @param count  item count for stack.
     * @return new created item stack or fail if it can't be created.
     */
    @Contract("null, _ -> fail")
    public @NotNull ItemStack newStack(@Nullable Object object, int count) {
        return newStack(object, count, WILDCARD_VALUE);
    }

    /**
     * Create new specific sized stack from Item, Block or registry name String with specified damage (meta).
     *
     * @param object item, must be Item, Block or registry name.
     * @param count  item count for stack.
     * @param damage stack damage.
     * @return new created item stack or fail if it can't be created.
     */
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

    /**
     * Generate hash code of stack with paramters from {@link UtilsItemStack#equals(ItemStack, ItemStack, int)}.
     *
     * @param stack  stack for hashing.
     * @param params equals params for taking properties from stack.
     * @return hash code of stack, 0 if stack is null, 1 if no parameters passed.
     */
    public int hashCode(@Nullable ItemStack stack, int params) {
        if (stack == null) return 0;
        var result = 1;
        if (Bits.isMaskOr(params, STACK_STRONG | ITEM | ITEM_STRONG))
            result = 31 * result + (stack.getItem() == null ? 0 : Item.getIdFromItem(stack.getItem()));
        if (Bits.isMaskOr(params, STACK_STRONG | COUNT | COUNT_STRONG))
            result = 31 * result + (Bits.isMaskAnd(params, COUNT) && stack.stackSize < 1 ? 0 : stack.stackSize);
        if (Bits.isMaskOr(params, STACK_STRONG | META | META_STRONG))
            result = 31 * result + (Bits.isMaskAnd(params, META) && stack.getItemDamage() == WILDCARD_VALUE ? 0 : stack.getItemDamage());
        if (Bits.isMaskOr(params, STACK_STRONG | NBT | NBT_STRONG))
            result = 31 * result + (Bits.isMaskAnd(params, NBT) && !stack.hasTagCompound() ? 0 : stack.getTagCompound().hashCode());
        return result;
    }
}
