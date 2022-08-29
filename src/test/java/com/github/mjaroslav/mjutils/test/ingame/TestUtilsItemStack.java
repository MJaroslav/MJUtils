package com.github.mjaroslav.mjutils.test.ingame;

import com.github.mjaroslav.mcingametester.api.BeforeClass;
import com.github.mjaroslav.mcingametester.api.Common;
import com.github.mjaroslav.mcingametester.api.Test;
import com.github.mjaroslav.mjutils.mod.lib.ModInfo;
import lombok.val;
import org.junit.Assert;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static com.github.mjaroslav.mjutils.util.game.item.UtilsItemStack.*;

@Common
public class TestUtilsItemStack {
    private static ItemStack testStackStick;
    private static ItemStack testStackDiamond;
    private static ItemStack testStackNull;
    private static ItemStack testStackItemNull;
    private static ItemStack testStackSizeZero;

    @BeforeClass
    public static void beforeClass() {
        testStackStick = new ItemStack(Items.stick, 1);
        testStackDiamond = new ItemStack(Items.diamond, 1);
        testStackNull = null;
        testStackItemNull = new ItemStack((Item) null, 1);
        testStackSizeZero = new ItemStack(Items.stick, 0);
    }

    @Test
    public void test$requireNonNullStackOrElse() {
        val expected = testStackStick;
        val expectedElse = testStackDiamond;
        val actual = requireNonNullStackOrElse(testStackStick, expectedElse);
        val actualNull = requireNonNullStackOrElse(testStackNull, expectedElse);
        val actualItemNull = requireNonNullStackOrElse(testStackItemNull, expectedElse);
        val actualSizeZero = requireNonNullStackOrElse(testStackSizeZero, expectedElse);
        Assert.assertTrue("Normal stack", isEquals(expected, actual));
        Assert.assertTrue("Null stack", isEquals(expectedElse, actualNull));
        Assert.assertTrue("Item null stack", isEquals(expectedElse, actualItemNull));
        Assert.assertTrue("Zero size stack", isEquals(expectedElse, actualSizeZero));
    }
}
