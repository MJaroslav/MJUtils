package io.github.mjaroslav.mjutils.util.game.item;

import com.github.mjaroslav.mcingametester.api.Assert;
import com.github.mjaroslav.mcingametester.api.BeforeClass;
import com.github.mjaroslav.mcingametester.api.Common;
import com.github.mjaroslav.mcingametester.api.Test;
import cpw.mods.fml.common.LoaderState;
import io.github.mjaroslav.mjutils.util.item.UtilsItemStack;
import lombok.val;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static io.github.mjaroslav.mjutils.util.item.UtilsItemStack.*;

@Common(when = LoaderState.INITIALIZATION)
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
        Assert.isTrue(UtilsItemStack.equals(expected, actual), "Normal stack");
        Assert.isTrue(UtilsItemStack.equals(expectedElse, actualNull), "Null stack");
        Assert.isTrue(UtilsItemStack.equals(expectedElse, actualItemNull), "Item null stack");
        Assert.isTrue(UtilsItemStack.equals(expectedElse, actualSizeZero), "Zero size stack");
    }
}
