package io.github.mjaroslav.mjutils.util.game.item;

import com.github.mjaroslav.mcingametester.api.Assert;
import com.github.mjaroslav.mcingametester.api.BeforeClass;
import com.github.mjaroslav.mcingametester.api.Common;
import com.github.mjaroslav.mcingametester.api.Test;
import cpw.mods.fml.common.LoaderState;
import io.github.mjaroslav.mjutils.item.Stacks;
import lombok.val;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static io.github.mjaroslav.mjutils.item.Stacks.*;

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
        val actual = anotherIfEmpty(testStackStick, expectedElse);
        val actualNull = anotherIfEmpty(testStackNull, expectedElse);
        val actualItemNull = anotherIfEmpty(testStackItemNull, expectedElse);
        val actualSizeZero = anotherIfEmpty(testStackSizeZero, expectedElse);
        Assert.isTrue(Stacks.equals(expected, actual), "Normal stack");
        Assert.isTrue(Stacks.equals(expectedElse, actualNull), "Null stack");
        Assert.isTrue(Stacks.equals(expectedElse, actualItemNull), "Item null stack");
        Assert.isTrue(Stacks.equals(expectedElse, actualSizeZero), "Zero size stack");
    }
}
