package mjaroslav.mcmods.mjutils.common.anvil;

import mjaroslav.mcmods.mjutils.common.utils.GameUtils;
import net.minecraft.item.ItemStack;

/**
 * 
 * @author MJaroslav
 *
 */
public class AnvilRecipe {
	/** Left anvil slot. */
	private ItemStack left;
	/** Right anvil slot. */
	private ItemStack right;
	/** Names: text field, left and right slots. */
	private String[] name = new String[] { "", "", "" };
	/** Switches for using names. */
	private boolean[] useName = new boolean[] { false, false, false };

	public AnvilRecipe(ItemStack left, ItemStack right) {
		this.left = left;
		this.right = right;
	}

	/**
	 * @return Left anvil item.
	 */
	public ItemStack getLeft() {
		return left;
	}

	/**
	 * @return Right anvil item.
	 */
	public ItemStack getRight() {
		return right;
	}

	public AnvilRecipe setName(String name) {
		this.name[0] = name;
		this.useName[0] = name.length() <= 0 ? false : true;
		return this;
	}

	/** Change mandatory name in text field. */
	public String getName() {
		return name[0];
	}

	/** Change mandatory name of left item. */
	public AnvilRecipe setLeftName(String leftName) {
		this.name[1] = leftName;
		this.useName[1] = leftName.length() <= 0 ? false : true;
		return this;
	}

	/**
	 * @return Name of left item.
	 */
	public String getLeftName() {
		return this.name[1];
	}

	/** Change mandatory name of right item. */
	public AnvilRecipe setRightName(String rightName) {
		this.name[2] = rightName;
		this.useName[2] = rightName.length() <= 0 ? false : true;
		return this;
	}

	/**
	 * @return Name of right item.
	 */
	public String getRightName() {
		return this.name[2];
	}

	/**
	 * Is the mandatory name used: text field.
	 */
	public boolean nameUsed() {
		return this.useName[0];
	}

	/**
	 * Is the mandatory name used: left item name.
	 */
	public boolean leftNameUsed() {
		return this.useName[1];
	}

	/**
	 * Is the mandatory name used: right item name.
	 */
	public boolean rightNameUsed() {
		return this.useName[2];
	}

	/**
	 * @param recipe
	 *            - second recipe.
	 * @return Recipes is equals.
	 */
	public boolean equals(AnvilRecipe recipe) {
		if (recipe == null)
			return false;
		if (this.left != recipe.left)
			return false;
		if (this.right != recipe.right)
			return false;
		for (int id = 0; id < 3; id++) {
			if (!this.name[0].equals(recipe.name[0]))
				return false;
			if (!this.useName[0] == recipe.useName[0])
				return false;
		}
		return true;
	}

	/**
	 * @return Ingredients are suitable for prescription.
	 */
	public boolean equals(ItemStack left, ItemStack right, String name) {
		if (this.nameUsed() && !(name.toLowerCase().replace(" ", "").equals(this.name[0])))
			return false;
		int count = 0;
		boolean flag1 = false;
		if (this.left == null && left == null)
			flag1 = true;
		else if (GameUtils.itemStackNotNullAndEquals(this.left, left)) {
			if (this.leftNameUsed() && !left.getDisplayName().toLowerCase().replace(" ", "").equals(this.getLeftName()))
				return false;
			if (this.left.stackSize < left.stackSize)
				if (left.stackSize % this.left.stackSize == 0)
					count = (int) left.stackSize / this.left.stackSize;
				else
					return false;
			flag1 = true;
		}
		boolean flag2 = false;
		if (this.right == null && right == null)
			flag2 = true;
		else if (GameUtils.itemStackNotNullAndEquals(this.right, right)) {
			if (this.rightNameUsed()
					&& !right.getDisplayName().toLowerCase().replace(" ", "").equals(this.getRightName()))
				return false;
			if (this.right.stackSize < right.stackSize)
				if (count * this.right.stackSize > right.stackSize)
					return false;
			flag2 = true;
		}
		return flag1 && flag2;
	}
}
