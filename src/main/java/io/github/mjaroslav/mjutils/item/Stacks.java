package io.github.mjaroslav.mjutils.item;

import cpw.mods.fml.common.registry.GameData;
import io.github.mjaroslav.sharedjava.format.Bits;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static net.minecraftforge.oredict.OreDictionary.*;

@UtilityClass
public class Stacks {
    public final int ITEM_NULLABLE = 0b000000001;
    public final int ITEM = 0b000000010;
    public final int COUNT_NONE = 0b000000100;
    public final int COUNT = 0b000001000;
    public final int META_WILDCARD = 0b000010000;
    public final int META = 0b000100000;
    public final int NBT_NULLABLE = 0b001000000;
    public final int NBT = 0b010000000;
    public final int NULLABLE = 0b100000000;

    public @NotNull ItemStack anotherIfEmpty(@Nullable ItemStack stack, @NotNull ItemStack defaultStack) {
        return isEmpty(stack) ? defaultStack : stack;
    }

    public @NotNull ItemStack airIfEmpty(@Nullable ItemStack stack) {
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
        return equals(a, b, ITEM_NULLABLE | META_WILDCARD);
    }

    public boolean equals(@Nullable ItemStack a, @Nullable ItemStack b) {
        return equals(a, b, ITEM_NULLABLE | COUNT_NONE | META_WILDCARD | NBT_NULLABLE);
    }

    public boolean equals(@Nullable ItemStack a, @Nullable ItemStack b, int params) {
        if (params == 0) return true;
        if (a == null || b == null) return Bits.isMaskAnd(params, NULLABLE) && a == b;
        var flagItem = true;
        var flagCount = true;
        var flagMeta = true;
        var flagNBT = true;
        val aItem = a.getItem();
        val bItem = b.getItem();
        if (Bits.isMaskAnd(params, ITEM)) flagItem = aItem != null && aItem == bItem;
        else if (Bits.isMaskAnd(params, ITEM_NULLABLE)) flagItem = aItem == bItem;
        int aSize = a.stackSize, bSize = b.stackSize;
        if (Bits.isMaskAnd(params, COUNT)) flagCount = aSize == bSize;
        else if (Bits.isMaskAnd(params, COUNT_NONE)) flagCount = aSize == bSize && aSize > 0;
        val aMeta = a.getItemDamage();
        val bMeta = b.getItemDamage();
        if (Bits.isMaskAnd(params, META)) flagMeta = aMeta == bMeta;
        else if (Bits.isMaskAnd(params, META_WILDCARD))
            flagMeta = aMeta == bMeta || aMeta == OreDictionary.WILDCARD_VALUE ||
                bMeta == OreDictionary.WILDCARD_VALUE;
        val aNBT = a.getTagCompound();
        val bNBT = b.getTagCompound();
        if (Bits.isMaskAnd(params, NBT)) flagNBT = aNBT != null && aNBT.equals(bNBT);
        else if (Bits.isMaskAnd(params, NBT_NULLABLE)) flagNBT = Objects.equals(aNBT, bNBT);
        return flagItem && flagCount && flagMeta && flagNBT;
    }

    @Contract("null -> fail")
    public @NotNull ItemStack make(@Nullable Object object) {
        return make(object, 1, WILDCARD_VALUE);
    }

    @Contract("null, _ -> fail")
    public @NotNull ItemStack make(@Nullable Object object, int count) {
        return make(object, count, WILDCARD_VALUE);
    }

    @Contract("null, _, _ -> fail")
    public @NotNull ItemStack make(@Nullable Object object, int count, int damage) {
        if (object instanceof Block block) return new ItemStack(block, count, damage);
        else if (object instanceof Item item) return new ItemStack(item, count, damage);
        else if (object instanceof ItemStack stack) {
            val result = stack.copy();
            result.stackSize = count;
            result.setItemDamage(damage);
            result.setTagCompound(null);
            return result;
        } else if (object instanceof String registryName)
            return new ItemStack(GameData.getItemRegistry().getObject(registryName), count, damage);
        else throw new IllegalArgumentException("Value must be Block, Item, ItemStack or String (with registry name)!");
    }

    public int hashCode(@Nullable ItemStack stack, int params) {
        if (stack == null) return 0;
        var result = 1;
        if (Bits.isMaskOr(params, ITEM_NULLABLE | ITEM))
            result = 31 * result + (stack.getItem() == null ? 0 : Item.getIdFromItem(stack.getItem()));
        if (Bits.isMaskOr(params, COUNT_NONE | COUNT))
            result = 31 * result + (Bits.isMaskAnd(params, COUNT_NONE) && stack.stackSize < 1 ? 0 : stack.stackSize);
        if (Bits.isMaskOr(params, META_WILDCARD | META))
            result = 31 * result + (Bits.isMaskAnd(params, META_WILDCARD) && stack.getItemDamage() == WILDCARD_VALUE ?
                0 : stack.getItemDamage());
        if (Bits.isMaskOr(params, NBT_NULLABLE | NBT))
            result = 31 * result + (!stack.hasTagCompound() ? 0 : stack.getTagCompound().hashCode());
        return result;
    }
}
