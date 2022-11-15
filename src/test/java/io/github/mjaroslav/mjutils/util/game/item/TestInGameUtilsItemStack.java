package io.github.mjaroslav.mjutils.util.game.item;

import com.github.mjaroslav.mcingametester.api.Assert;
import com.github.mjaroslav.mcingametester.api.BeforeClass;
import com.github.mjaroslav.mcingametester.api.Common;
import com.github.mjaroslav.mcingametester.api.Test;
import lombok.val;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static io.github.mjaroslav.mjutils.util.game.item.UtilsItemStack.isEquals;
import static io.github.mjaroslav.mjutils.util.game.item.UtilsItemStack.requireNonNullStackOrElse;

@Common
public class TestInGameUtilsItemStack {
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
        Assert.isTrue(isEquals(expected, actual), "Normal stack");
        Assert.isTrue(isEquals(expectedElse, actualNull), "Null stack");
        Assert.isTrue(isEquals(expectedElse, actualItemNull), "Item null stack");
        Assert.isTrue(isEquals(expectedElse, actualSizeZero), "Zero size stack");
    }
}
