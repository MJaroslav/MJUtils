package mjaroslav.mcmods.mjutils.lib.handler;

import static codechicken.lib.gui.GuiDraw.*;
import static codechicken.nei.NEIServerUtils.*;
import static net.minecraft.util.StringUtils.*;

import java.awt.Rectangle;
import java.util.*;

import com.mojang.realmsclient.gui.ChatFormatting;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import mjaroslav.mcmods.mjutils.lib.utils.UtilsAnvil;
import mjaroslav.mcmods.mjutils.mod.lib.ModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

/**
 * Anvil recipe NEI handler.
 *
 * @author MJaroslav
 */
public class NEIAnvilRecipeHandler extends TemplateRecipeHandler {
    @Override
    public String getRecipeName() {
        return I18n.format("container.repair");
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiRepair.class;
    }

    @Override
    public void loadTransferRects() {
        this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(97, 37, 22, 15), "anvil"));
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if ((outputId.equals("anvil")) && (super.getClass() == NEIAnvilRecipeHandler.class)) {
            Map<AnvilRecipe, ItemStack> recipes = UtilsAnvil.getRecipes();
            for (Map.Entry<AnvilRecipe, ItemStack> recipe : recipes.entrySet())
                arecipes.add(new AnvilPair(recipe.getKey(), recipe.getValue()));
        } else
            super.loadCraftingRecipes(outputId, results);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        Map<AnvilRecipe, ItemStack> recipes = UtilsAnvil.getRecipes();
        for (Map.Entry<AnvilRecipe, ItemStack> recipe : recipes.entrySet())
            if (areStacksSameType(recipe.getValue(), result))
                arecipes.add(new AnvilPair(recipe.getKey(), recipe.getValue()));
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        Map<AnvilRecipe, ItemStack> recipes = UtilsAnvil.getRecipes();
        for (Map.Entry<AnvilRecipe, ItemStack> recipe : recipes.entrySet())
            if (areStacksSameTypeCrafting(recipe.getKey().leftStack, ingredient)) {
                AnvilPair arecipe = new AnvilPair(recipe.getKey(), recipe.getValue());
                arecipe.setIngredientPermutation(Arrays.asList(arecipe.leftStack, arecipe.rightStack), ingredient);
                arecipes.add(arecipe);
            } else if (areStacksSameTypeCrafting(recipe.getKey().rightStack, ingredient)) {
                AnvilPair arecipe = new AnvilPair(recipe.getKey(), recipe.getValue());
                arecipe.setIngredientPermutation(Arrays.asList(arecipe.leftStack, arecipe.rightStack), ingredient);
                arecipes.add(arecipe);
            }
    }

    @Override
    public String getGuiTexture() {
        return new ResourceLocation(ModInfo.MODID, "textures/gui/nei/anvil.png").toString();
    }

    @Override
    public void drawExtras(int recipe) {
        int k = 8453920;
        String s;
        if (((AnvilPair) arecipes.get(recipe)).cost > 0)
            s = I18n.format("container.repair.cost", ((AnvilPair) arecipes.get(recipe)).cost);
        else
            s = I18n.format("container.repair.cost", 1) + " "
                    + StatCollector.translateToLocal("container.repair.cost.always");
        FontRenderer fonts = Minecraft.getMinecraft().fontRenderer;
        int l = -16777216 | (k & 16579836) >> 2 | k & -16777216;
        int i1 = 176 - 8 - fonts.getStringWidth(s);
        byte b0 = 67;
        if (!fonts.getUnicodeFlag()) {
            fonts.drawString(s, i1 - 5, b0 + 1 - 12, l);
            fonts.drawString(s, i1 + 1 - 5, b0 - 12, l);
            fonts.drawString(s, i1 + 1 - 5, b0 + 1 - 12, l);
        }
        fonts.drawString(s, i1 - 5, b0 - 12, k);
        s = ((AnvilPair) arecipes.get(recipe)).textField;
        if (!isNullOrEmpty(s))
            fonts.drawStringWithShadow(s, 57, 13, 14737632);
    }

    @Override
    public void drawBackground(int recipe) {
        super.drawBackground(recipe);
        String name = ((AnvilPair) arecipes.get(recipe)).textField;
        String s;
        if (((AnvilPair) arecipes.get(recipe)).cost > 0)
            s = I18n.format("container.repair.cost", ((AnvilPair) arecipes.get(recipe)).cost);
        else
            s = I18n.format("container.repair.cost", 1) + " "
                    + StatCollector.translateToLocal("container.repair.cost.always");
        int offset = !isNullOrEmpty(name) ? 0 : 16;
        drawTexturedModalRect(54, 9, 0, offset + 166, 110, 16);
        FontRenderer fonts = Minecraft.getMinecraft().fontRenderer;
        if (fonts.getUnicodeFlag()) {
            int i1 = 176 - 8 - fonts.getStringWidth(s);
            byte b0 = 67;
            drawRect(i1 - 8, b0 - 14, 176 - 12 - (i1 - 8), b0 - 1 - (b0 - 13), -16777216);
            drawRect(i1 - 7, b0 - 13, 176 - 13 - (i1 - 7), b0 - 2 - (b0 - 12), -12895429);
        }
        if (((AnvilPair) arecipes.get(recipe)).leftStackHasName)
            drawTexturedModalRect(21, 35, 0, 198, 18, 18);
        if (((AnvilPair) arecipes.get(recipe)).rightStackHasName)
            drawTexturedModalRect(70, 35, 0, 198, 18, 18);
    }

    @Override
    public String getOverlayIdentifier() {
        return "anvil";
    }

    /**
     * Anvil recipe blank in NEI helping.
     *
     * @author MJaroslav
     */
    public class AnvilPair extends TemplateRecipeHandler.CachedRecipe {
        public int cost;
        public String textField;
        public boolean leftStackHasName;
        public boolean rightStackHasName;
        PositionedStack leftStack;
        PositionedStack rightStack;
        PositionedStack result;

        public AnvilPair(AnvilRecipe recipe, ItemStack result) {
            super();
            ItemStack left = recipe.leftStack.copy();
            if (!isNullOrEmpty(recipe.leftStackName)) {
                left.setStackDisplayName(ChatFormatting.UNDERLINE.toString() + recipe.leftStackName);
                leftStackHasName = true;
            }
            ItemStack right = recipe.rightStack.copy();
            if (!isNullOrEmpty(recipe.rightStackName)) {
                right.setStackDisplayName(ChatFormatting.UNDERLINE.toString() + recipe.rightStackName);
                rightStackHasName = true;
            }
            leftStack = new PositionedStack(left, 22, 36);
            rightStack = new PositionedStack(right, 71, 36);
            this.result = new PositionedStack(result, 129, 36);
            cost = recipe.cost;
            textField = recipe.textField;
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return Arrays.asList(leftStack, rightStack);
        }

        @Override
        public PositionedStack getResult() {
            return result;
        }
    }
}