package com.github.mjaroslav.mjutils.test.util.game;

import lombok.val;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.github.mjaroslav.mjutils.util.game.item.UtilsItemStack.*;

public class TestUtilsItemStack {
    public static Item testItem1;
    public static Item testItem2;

    @BeforeClass
    public static void init() {
        //MixinBootstrap.init();
        //MixinExtrasBootstrap.init();
        /*AccessorBlocks.setAir(new Block(Material.air) {
            @Override
            public String toString() {
                return getUnlocalizedName();
            }
        }.setBlockName("air"));*/
        testItem1 = new Item() {
            @Override
            public String toString() {
                return getUnlocalizedName();
            }
        }.setUnlocalizedName("testItem1");
        testItem2 = new Item() {
            @Override
            public String toString() {
                return getUnlocalizedName();
            }
        }.setUnlocalizedName("testItem2");
    }

    @Test
    public void test$isNotEmpty() {
        val normal = new ItemStack(testItem1, 1);
        val nullItem = new ItemStack((Item) null, 1);
        val zeroStackSize = new ItemStack(testItem2, 0);
        Assert.assertTrue("Normal stack", isNotEmpty(normal));
        Assert.assertFalse("Item type null", isNotEmpty(nullItem));
        Assert.assertFalse("Stacksize less than 1", isNotEmpty(zeroStackSize));
    }

    @Test
    public void test$isEmpty() {
        val normal = new ItemStack(testItem1, 1);
        val nullItem = new ItemStack((Item) null, 1);
        val zeroStackSize = new ItemStack(testItem2, 0);
        Assert.assertFalse("Normal stack", isEmpty(normal));
        Assert.assertTrue("Item type null", isEmpty(nullItem));
        Assert.assertTrue("Stacksize less than 1", isEmpty(zeroStackSize));
    }

    @Test
    public void test$requireNonNullStackOrElse() {
        val requiredNormal = new ItemStack(testItem1, 1, 5);
        val another = new ItemStack(testItem1, 1, 5);
        Assert.assertTrue("Normal stack", isEquals(requiredNormal, requireNonNullStackOrElse(requiredNormal, another)));
        Assert.assertTrue("Null stack", isEquals(another, requireNonNullStackOrElse(null, another)));
    }

    @Test
    public void test$requireNonNullStack() {
        val requiredNormal = new ItemStack(testItem1, 1, 5);
        Assert.assertTrue("Normal stack", isEquals(requiredNormal, requireNonNullStack(requiredNormal)));
        //Assert.assertTrue("Null stack", isNotEmpty(requireNonNullStack(null)));
    }
}
