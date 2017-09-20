package mjaroslav.mcmods.mjutils.common.anvil;

import mjaroslav.mcmods.mjutils.common.utils.GameUtils;
import net.minecraft.item.ItemStack;

public class AnvilRecipe {
	private ItemStack left;
	private ItemStack right;
	private String[] name = new String[] { "", "", "" };
	private boolean[] useName = new boolean[] { false, false, false };

	public AnvilRecipe(ItemStack left, ItemStack right) {
		this.left = left;
		this.right = right;
	}

	public ItemStack getLeft() {
		return left;
	}

	public ItemStack getRight() {
		return right;
	}

	public AnvilRecipe setName(String name) {
		this.name[0] = name;
		this.useName[0] = name.length() <= 0 ? false : true;
		return this;
	}

	public String getName() {
		return name[0];
	}

	public AnvilRecipe setLeftName(String leftName) {
		this.name[1] = leftName;
		this.useName[1] = leftName.length() <= 0 ? false : true;
		return this;
	}

	public String getLeftName() {
		return this.name[1];
	}

	public AnvilRecipe setRightName(String rightName) {
		this.name[2] = rightName;
		this.useName[2] = rightName.length() <= 0 ? false : true;
		return this;
	}

	public String getRightName() {
		return this.name[2];
	}

	public boolean nameUsed() {
		return this.useName[0];
	}

	public boolean leftNameUsed() {
		return this.useName[1];
	}

	public boolean rightNameUsed() {
		return this.useName[2];
	}

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
