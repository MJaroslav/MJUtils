package mjaroslav.mcmods.mjutils.common.anvil;

import mjaroslav.mcmods.mjutils.common.utils.GameUtils;
import mjaroslav.mcmods.mjutils.common.utils.OtherUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;

public class AnvilRecipe {
	public ItemStack leftStack;

	public ItemStack rightStack;

	public String leftStackName;

	public String rightStackName;

	public String textField;

	public int cost;

	public AnvilRecipe(ItemStack leftStack, ItemStack rightStack) {
		this(leftStack, rightStack, "", "", "", 1);
	}

	public AnvilRecipe(ItemStack leftStack, ItemStack rightStack, int cost) {
		this(leftStack, rightStack, "", "", "", cost);
	}

	public AnvilRecipe(ItemStack leftStack, ItemStack rightStack, String textField) {
		this(leftStack, rightStack, "", "", textField, 1);
	}

	public AnvilRecipe(ItemStack leftStack, ItemStack rightStack, String textField, int cost) {
		this(leftStack, rightStack, "", "", textField, cost);
	}

	public AnvilRecipe(ItemStack leftStack, ItemStack rightStack, String leftStackName, String rightStackName,
			String textField) {
		this(leftStack, rightStack, leftStackName, rightStackName, textField, 1);
	}

	public AnvilRecipe(ItemStack leftStack, ItemStack rightStack, String leftStackName, String rightStackName,
			String textField, int cost) {
		this.leftStack = leftStack;
		this.rightStack = rightStack;
		this.leftStackName = leftStackName;
		this.rightStackName = rightStackName;
		this.textField = textField;
		this.cost = cost;
	}

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
