package mjaroslav.mcmods.mjutils.common.anvil;

import mjaroslav.mcmods.mjutils.common.utils.GameUtils;
import net.minecraft.item.ItemStack;

/**
 * Blank for anvil recipe.
 * 
 * @author MJaroslav
 * 
 */
public class AnvilRecipe {
	/**
	 * Left anvil slot.
	 */
	private ItemStack left;
	/**
	 * Right anvil slot.
	 */
	private ItemStack right;
	/**
	 * Names: anvil text field, left and right slots. Format: lower case without
	 * spaces.
	 */
	private String[] name = new String[] { "", "", "" };
	/**
	 * Switches for using names.
	 */
	private boolean[] useName = new boolean[] { false, false, false };

	/**
	 * New anvil recipe.
	 * 
	 * @param left
	 *            - item in left anvil slot.
	 * @param right
	 *            - item in right anvil slot.
	 */
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

	/**
	 * Change mandatory name of anvil text field.
	 * 
	 * @param name
	 *            - format: lower case without spaces.
	 * @return AnvilRecipe with new mandatory name of text field .
	 */
	public AnvilRecipe setName(String name) {
		this.name[0] = name;
		this.useName[0] = name.length() <= 0 ? false : true;
		return this;
	}

	/**
	 * @return Mandatory name in text field.
	 */
	public String getName() {
		return name[0];
	}

	/**
	 * Change mandatory name of left item.
	 * 
	 * @param leftName
	 *            - format: lower case without spaces.
	 * @return AnvilRecipe with new left item name.
	 */
	public AnvilRecipe setLeftName(String leftName) {
		this.name[1] = leftName;
		this.useName[1] = leftName.length() <= 0 ? false : true;
		return this;
	}

	/**
	 * Get mandatory name of right item.
	 * 
	 * @return Left item name in format: lower case without spaces.
	 */
	public String getLeftName() {
		return this.name[1];
	}

	/**
	 * Change mandatory name of right item.
	 * 
	 * @param rightName
	 *            - format: lower case without spaces.
	 * @return AnvilRecipe with new right item name.
	 */
	public AnvilRecipe setRightName(String rightName) {
		this.name[2] = rightName;
		this.useName[2] = rightName.length() <= 0 ? false : true;
		return this;
	}

	/**
	 * Get mandatory name of right item.
	 * 
	 * @return Right item name in format: lower case without spaces.
	 */
	public String getRightName() {
		return this.name[2];
	}

	/**
	 * Is the mandatory name used: text field.
	 * 
	 * @return True if the text field must contain text.
	 */
	public boolean nameUsed() {
		return this.useName[0];
	}

	/**
	 * Is the mandatory name used: left item.
	 * 
	 * @return True if the left item has to be with a certain name.
	 */
	public boolean leftNameUsed() {
		return this.useName[1];
	}

	/**
	 * Is the mandatory name used: right item.
	 * 
	 * @return True if the right item has to be with a certain name.
	 */
	public boolean rightNameUsed() {
		return this.useName[2];
	}

	/**
	 * Compare two AnvilRecipe.
	 * 
	 * @param recipe
	 *            - second recipe.
	 * @return True if recipes is equals.
	 */
	public boolean equals(AnvilRecipe recipe) {
		if (recipe == null)
			return false;
		if (this.left != recipe.left)
			return false;
		if (this.right != recipe.right)
			return false;
		for (int id = 0; id < 3; id++) {
			if (!this.name[id].equals(recipe.name[id]))
				return false;
			if (!this.useName[id] == recipe.useName[id])
				return false;
		}
		return true;
	}

	/**
	 * Compare two AnvilRecipe.
	 * 
	 * @param left
	 *            - item in left slot.
	 * @param right
	 *            - item in right slot.
	 * @param name
	 *            - text field of anvil.
	 * @return True if ingredients are suitable for this recipe.
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
