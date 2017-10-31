package mjaroslav.mcmods.mjutils.common.anvil;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.mojang.realmsclient.gui.ChatFormatting;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import mjaroslav.mcmods.mjutils.common.anvil.AnvilUtils.AnvilResult;
import mjaroslav.mcmods.mjutils.lib.MJInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

/**
 * Anvil recipe NEI handler.
 * 
 * @author MJaroslav
 *
 */
public class NEIAnvilRecipeHandler extends TemplateRecipeHandler {
	@Override
	public String getRecipeName() {
		return I18n.format("container.repair", new Object[0]);
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return GuiRepair.class;
	}

	@Override
	public void loadTransferRects() {
		this.transferRects.add(
				new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(97, 37, 22, 15), "anvil", new Object[0]));
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if ((outputId.equals("anvil")) && (super.getClass() == NEIAnvilRecipeHandler.class)) {
			Map<AnvilRecipe, AnvilResult> recipes = AnvilUtils.instance().getRecipes();
			for (Map.Entry<AnvilRecipe, AnvilResult> recipe : recipes.entrySet()) {
				this.arecipes.add(new AnvilPair(recipe.getKey(), recipe.getValue()));
			}
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		Map<AnvilRecipe, AnvilResult> recipes = AnvilUtils.instance().getRecipes();
		for (Map.Entry<AnvilRecipe, AnvilResult> recipe : recipes.entrySet())
			if (NEIServerUtils.areStacksSameType(recipe.getValue().result, result))
				this.arecipes.add(new AnvilPair(recipe.getKey(), recipe.getValue()));
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		Map<AnvilRecipe, AnvilResult> recipes = AnvilUtils.instance().getRecipes();
		for (Map.Entry<AnvilRecipe, AnvilResult> recipe : recipes.entrySet())
			if (NEIServerUtils.areStacksSameTypeCrafting(recipe.getKey().getLeft(), ingredient)) {
				AnvilPair arecipe = new AnvilPair(recipe.getKey(), recipe.getValue());
				arecipe.setIngredientPermutation(Arrays.asList(new PositionedStack[] { arecipe.left, arecipe.right }),
						ingredient);
				this.arecipes.add(arecipe);
			} else if (NEIServerUtils.areStacksSameTypeCrafting(recipe.getKey().getRight(), ingredient)) {
				AnvilPair arecipe = new AnvilPair(recipe.getKey(), recipe.getValue());
				arecipe.setIngredientPermutation(Arrays.asList(new PositionedStack[] { arecipe.left, arecipe.right }),
						ingredient);
				this.arecipes.add(arecipe);
			}
	}

	@Override
	public String getGuiTexture() {
		return new ResourceLocation(MJInfo.MODID, "textures/gui/nei/anvil.png").toString();
	}

	@Override
	public void drawExtras(int recipe) {
		int k = 8453920;
		String s = I18n.format("container.repair.cost",
				new Object[] { Integer.valueOf(((AnvilPair) this.arecipes.get(recipe)).cost) });
		FontRenderer fonts = Minecraft.getMinecraft().fontRenderer;
		if (((AnvilPair) this.arecipes.get(recipe)).cost > 0) {
			int l = -16777216 | (k & 16579836) >> 2 | k & -16777216;
			int i1 = 176 - 8 - fonts.getStringWidth(s);
			byte b0 = 67;
			if (!fonts.getUnicodeFlag()) {
				fonts.drawString(s, i1 - 5, b0 + 1 - 12, l);
				fonts.drawString(s, i1 + 1 - 5, b0 - 12, l);
				fonts.drawString(s, i1 + 1 - 5, b0 + 1 - 12, l);
			}
			fonts.drawString(s, i1 - 5, b0 - 12, k);
		}
		s = ((AnvilPair) this.arecipes.get(recipe)).name;
		if (!StringUtils.isNullOrEmpty(s))
			fonts.drawStringWithShadow(s, 57, 13, 14737632);
	}

	@Override
	public void drawBackground(int recipe) {
		super.drawBackground(recipe);
		int k = 8453920;
		String name = ((AnvilPair) this.arecipes.get(recipe)).name;
		String s = I18n.format("container.repair.cost",
				new Object[] { Integer.valueOf(((AnvilPair) this.arecipes.get(recipe)).cost) });
		int offset = !StringUtils.isNullOrEmpty(name) ? 0 : 16;
		GuiDraw.drawTexturedModalRect(54, 9, 0, offset + 166, 110, 16);
		FontRenderer fonts = Minecraft.getMinecraft().fontRenderer;
		if (((AnvilPair) this.arecipes.get(recipe)).cost > 0)
			if (fonts.getUnicodeFlag()) {
				int i1 = 176 - 8 - fonts.getStringWidth(s);
				byte b0 = 67;
				GuiDraw.drawRect(i1 - 8, b0 - 14, 176 - 12 - (i1 - 8), b0 - 1 - (b0 - 13), -16777216);
				GuiDraw.drawRect(i1 - 7, b0 - 13, 176 - 13 - (i1 - 7), b0 - 2 - (b0 - 12), -12895429);
			}
		if (((AnvilPair) this.arecipes.get(recipe)).lNamed) {
			GuiDraw.drawTexturedModalRect(21, 35, 0, 198, 18, 18);
		}
		if (((AnvilPair) this.arecipes.get(recipe)).rNamed) {
			GuiDraw.drawTexturedModalRect(70, 35, 0, 198, 18, 18);
		}
	}

	@Override
	public String getOverlayIdentifier() {
		return "anvil";
	}

	/**
	 * Anvil recipe blank in NEI helping.
	 * 
	 * @author MJaroslav
	 *
	 */
	public class AnvilPair extends TemplateRecipeHandler.CachedRecipe {
		PositionedStack left;
		PositionedStack right;
		PositionedStack result;
		/** Exp levels */
		public int cost;
		public String name;
		public boolean lNamed;
		public boolean rNamed;

		public AnvilPair(AnvilRecipe recipe, AnvilResult result) {
			super();
			ItemStack left = recipe.getLeft().copy();
			if (recipe.leftNameUsed()) {
				left.setStackDisplayName(ChatFormatting.UNDERLINE.toString() + recipe.getLeftName());
				this.lNamed = true;
			}
			ItemStack right = recipe.getRight().copy();
			if (recipe.rightNameUsed()) {
				right.setStackDisplayName(ChatFormatting.UNDERLINE.toString() + recipe.getRightName());
				this.rNamed = true;
			}
			this.left = new PositionedStack(left, 22, 36);
			this.right = new PositionedStack(right, 71, 36);
			this.result = new PositionedStack(result.result, 129, 36);
			this.cost = result.levels;
			this.name = recipe.getName();
		}

		@Override
		public List<PositionedStack> getIngredients() {
			return Arrays.asList(new PositionedStack[] { this.left, this.right });
		}

		@Override
		public PositionedStack getResult() {
			return this.result;
		}
	}
}