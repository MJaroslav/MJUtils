package mjaroslav.mcmods.mjutils.common.anvil;

import mjaroslav.mcmods.mjutils.common.utils.GameUtils;
import mjaroslav.mcmods.mjutils.common.utils.OtherUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;

/**
 * The recipe for the anvil. Can take into account the renaming line, the names
 * of the items and stack size in the second slot.
 * 
 * @author MJaroslav
 *
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
	 * @see AnvilRecipe
	 * @param leftStack
	 *            - see {@link #leftStack}.
	 * @param rightStack
	 *            - see {@link #rightStack}.
	 */
	public AnvilRecipe(ItemStack leftStack, ItemStack rightStack) {
		this(leftStack, rightStack, "", "", "", 0);
	}

	/**
	 * @see AnvilRecipe
	 * @param leftStack
	 *            - see {@link #leftStack}.
	 * @param rightStack
	 *            - see {@link #rightStack}.
	 * @param cost
	 *            - see {@link #cost}.
	 */
	public AnvilRecipe(ItemStack leftStack, ItemStack rightStack, int cost) {
		this(leftStack, rightStack, "", "", "", cost);
	}

	/**
	 * @see AnvilRecipe
	 * @param leftStack
	 *            - see {@link #leftStack}.
	 * @param rightStack
	 *            - see {@link #rightStack}.
	 * @param textField
	 *            - see {@link #textField}.
	 */
	public AnvilRecipe(ItemStack leftStack, ItemStack rightStack, String textField) {
		this(leftStack, rightStack, "", "", textField, 0);
	}

	/**
	 * @see AnvilRecipe
	 * @param leftStack
	 *            - see {@link #leftStack}.
	 * @param rightStack
	 *            - see {@link #rightStack}.
	 * @param textField
	 *            - see {@link #textField}.
	 * @param cost
	 *            - see {@link #cost}.
	 */
	public AnvilRecipe(ItemStack leftStack, ItemStack rightStack, String textField, int cost) {
		this(leftStack, rightStack, "", "", textField, cost);
	}

	/**
	 * @see AnvilRecipe
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
	 */
	public AnvilRecipe(ItemStack leftStack, ItemStack rightStack, String leftStackName, String rightStackName,
			String textField) {
		this(leftStack, rightStack, leftStackName, rightStackName, textField, 0);
	}

	/**
	 * @see AnvilRecipe
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
		if (recipe == null)
			return false;
		if (!GameUtils.itemStackNotNullAndEquals(this.leftStack, recipe.leftStack))
			return false;
		if (!GameUtils.itemStackNotNullAndEquals(this.rightStack, recipe.rightStack))
			return false;
		if (!OtherUtils.string(this.leftStackName).equals(OtherUtils.string(recipe.leftStackName)))
			return false;
		if (!OtherUtils.string(this.rightStackName).equals(OtherUtils.string(recipe.rightStackName)))
			return false;
		if (!OtherUtils.string(this.textField).equals(OtherUtils.string(recipe.textField)))
			return false;
		if (this.cost != recipe.cost)
			return false;
		return true;
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
		if (!GameUtils.itemStackNotNullAndEquals(this.leftStack, leftStack))
			return false;
		if (!GameUtils.itemStackNotNullAndEquals(this.rightStack, rightStack))
			return false;
		if (!StringUtils.isNullOrEmpty(this.leftStackName)
				&& !OtherUtils.string(OtherUtils.nameFormat(leftStack)).equals(OtherUtils.string(this.leftStackName)))
			return false;
		if (!StringUtils.isNullOrEmpty(this.rightStackName)
				&& !OtherUtils.string(OtherUtils.nameFormat(rightStack)).equals(OtherUtils.string(this.rightStackName)))
			return false;
		if (!StringUtils.isNullOrEmpty(this.textField)
				&& !OtherUtils.string(this.textField).equals(OtherUtils.string(textField)))
			return false;
		if (cost == -1)
			return true;
		if (this.cost != cost)
			return false;
		return true;
	}

	public AnvilRecipe copy() {
		AnvilRecipe recipe = new AnvilRecipe(leftStack.copy(), rightStack.copy(), leftStackName, rightStackName,
				textField, cost);
		return recipe;
	}
}
