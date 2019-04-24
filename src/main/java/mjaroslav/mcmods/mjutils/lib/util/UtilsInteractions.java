package mjaroslav.mcmods.mjutils.lib.util;

import mjaroslav.mcmods.mjutils.lib.object.item.ItemStackSet;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.oredict.OreDictionary;

public class UtilsInteractions {
    private static final ItemStackSet DISABLED_BREAKING_IN_CREATIVE = new ItemStackSet(false, true, false);
    private static final ItemStackSet PIGZOMBIE_TRIGGER_BLOCKS = new ItemStackSet(false, true, false);

    public static void setDisableBlockBreakingInCreative(Item item, boolean value) {
        setDisableBlockBreakingInCreative(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE), value);
    }

    public static void setDisableBlockBreakingInCreative(Block block, boolean value) {
        setDisableBlockBreakingInCreative(new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE), value);
    }

    public static void setDisableBlockBreakingInCreative(ItemStack stack, boolean value) {
        if (value)
            DISABLED_BREAKING_IN_CREATIVE.add(stack);
        else DISABLED_BREAKING_IN_CREATIVE.remove(stack);
    }

    public static boolean blockBreakingIsDisabledInCreative(ItemStack stack) {
        return !UtilsInventory.itemStackNotNull(stack) || (stack.getItem() instanceof ItemSword) ||
                DISABLED_BREAKING_IN_CREATIVE.contains(stack);
    }

    public static void setPigzombieTriggerBlock(Block block, boolean value) {
        setPigzombieTriggerBlock(block, OreDictionary.WILDCARD_VALUE, value);
    }

    public static void setPigzombieTriggerBlock(Block block, int meta, boolean value) {
        if (value)
            PIGZOMBIE_TRIGGER_BLOCKS.add(new ItemStack(block, 1, meta));
        else PIGZOMBIE_TRIGGER_BLOCKS.remove(new ItemStack(block, 1, meta));
    }

    public static boolean blockIsPigzombieTrigger(Block block, int meta) {
        return PIGZOMBIE_TRIGGER_BLOCKS.contains(new ItemStack(block, 1, meta));
    }
}
