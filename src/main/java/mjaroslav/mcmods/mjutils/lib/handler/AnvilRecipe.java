package mjaroslav.mcmods.mjutils.lib.handler;

import static mjaroslav.mcmods.mjutils.lib.utils.UtilsGame.*;
import static mjaroslav.utils.UtilsJava.*;
import static net.minecraft.util.StringUtils.*;

import net.minecraft.item.ItemStack;

/**
 * The recipe for the anvil. Can take into account the renaming line, the names
 * of the items and stack size in the second slot.
 *
 * @author MJaroslav
 */
public class AnvilRecipe {
    /**
     * ItemStack from left anvil slot. Stack size must be 1.
     */
    public ItemStack leftStack;

    /**
     * ItemStack from right anvil slot. Stack size can be any.
     */
    public ItemStack rightStack;

    /**
     * Name of left stack in lower case without spaces. If the string is empty,
     * it is ignored.
     */
    public String leftStackName;

    /**
     * Name of left stack in lower case without spaces. If the string is empty,
     * it is ignored.
     */
    public String rightStackName;

    /**
     * Anvil text field. If the string is empty, it is ignored.
     */
    public String textField;

    /**
     * The price of the recipe in the levels. If it is zero, the price will
     * always be equal to one, not counting the quantity.
     */
    public int cost;

    /**
     * @param leftStack
     *            - see {@link #leftStack}.
     * @param rightStack
     *            - see {@link #rightStack}.
     * @see AnvilRecipe
     */
    public AnvilRecipe(ItemStack leftStack, ItemStack rightStack) {
        this(leftStack, rightStack, "", "", "", 0);
    }

    /**
     * @param leftStack
     *            - see {@link #leftStack}.
     * @param rightStack
     *            - see {@link #rightStack}.
     * @param cost
     *            - see {@link #cost}.
     * @see AnvilRecipe
     */
    public AnvilRecipe(ItemStack leftStack, ItemStack rightStack, int cost) {
        this(leftStack, rightStack, "", "", "", cost);
    }

    /**
     * @param leftStack
     *            - see {@link #leftStack}.
     * @param rightStack
     *            - see {@link #rightStack}.
     * @param textField
     *            - see {@link #textField}.
     * @see AnvilRecipe
     */
    public AnvilRecipe(ItemStack leftStack, ItemStack rightStack, String textField) {
        this(leftStack, rightStack, "", "", textField, 0);
    }

    /**
     * @param leftStack
     *            - see {@link #leftStack}.
     * @param rightStack
     *            - see {@link #rightStack}.
     * @param textField
     *            - see {@link #textField}.
     * @param cost
     *            - see {@link #cost}.
     * @see AnvilRecipe
     */
    public AnvilRecipe(ItemStack leftStack, ItemStack rightStack, String textField, int cost) {
        this(leftStack, rightStack, "", "", textField, cost);
    }

    /**
     * @param leftStack
     *            - see {@link #leftStack}.
     * @param rightStack
     *            - see {@link #rightStack}.
     * @param leftStackName
     *            - see {@link #leftStackName}.
     * @param rightStackName
     *            - see {@link #rightStackName}.
     * @param textField
     *            - see {@link #textField}.
     * @see AnvilRecipe
     */
    public AnvilRecipe(ItemStack leftStack, ItemStack rightStack, String leftStackName, String rightStackName,
            String textField) {
        this(leftStack, rightStack, leftStackName, rightStackName, textField, 0);
    }

    /**
     * @param leftStack
     *            - see {@link #leftStack}.
     * @param rightStack
     *            - see {@link #rightStack}.
     * @param leftStackName
     *            - see {@link #leftStackName}.
     * @param rightStackName
     *            - see {@link #rightStackName}.
     * @param textField
     *            - see {@link #textField}.
     * @param cost
     *            - see {@link #cost}.
     * @see AnvilRecipe
     */
    public AnvilRecipe(ItemStack leftStack, ItemStack rightStack, String leftStackName, String rightStackName,
            String textField, int cost) {
        this.leftStack = leftStack;
        this.rightStack = rightStack;
        this.leftStackName = leftStackName;
        this.rightStackName = rightStackName;
        this.textField = textField;
        this.cost = cost;
    }

    /**
     * Check the consistency of the two recipes.
     *
     * @param recipe
     *            - recipe for testing.
     * @return True if this object is the same as the obj argument; false
     *         otherwise.
     */
    public boolean equals(AnvilRecipe recipe) {
        return recipe != null && itemStackNotNullAndEquals(leftStack, recipe.leftStack)
                && itemStackNotNullAndEquals(rightStack, recipe.rightStack)
                && string(leftStackName).equals(string(recipe.leftStackName))
                && string(rightStackName).equals(string(recipe.rightStackName))
                && string(textField).equals(string(recipe.textField)) && cost == recipe.cost;
    }

    /**
     * Check the consistency of the two recipes.
     *
     * @param leftStack
     *            - see {@link #leftStack}.
     * @param rightStack
     *            - see {@link #rightStack}.
     * @param textField
     *            - see {@link #textField}.
     * @param cost
     *            - see {@link #cost}. If it is minus one, it is not taken into
     *            account.
     * @return True if this object is the same as the obj argument; false
     *         otherwise.
     */
    public boolean equals(ItemStack leftStack, ItemStack rightStack, String textField, int cost) {
        return itemStackNotNullAndEquals(this.leftStack, leftStack)
                && itemStackNotNullAndEquals(this.rightStack, rightStack)
                && (isNullOrEmpty(leftStackName)
                        || string(nameFormat(leftStack)).equals(string(leftStackName)))
                && (isNullOrEmpty(rightStackName)
                        || string(nameFormat(rightStack)).equals(string(rightStackName)))
                && (isNullOrEmpty(this.textField) || string(this.textField).equals(string(textField)))
                && (cost == -1 || this.cost == cost);
    }

    public AnvilRecipe copy() {
        return new AnvilRecipe(leftStack.copy(), rightStack.copy(), leftStackName, rightStackName, textField, cost);
    }
}
