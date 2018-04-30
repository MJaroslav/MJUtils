package mjaroslav.mcmods.mjutils.lib.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import mjaroslav.mcmods.mjutils.lib.handler.AnvilRecipe;
import net.minecraft.item.ItemStack;

@Cancelable
public class AnvilCraftingEvent extends Event {
    public AnvilRecipe recipe;
    public ItemStack result;

    public AnvilCraftingEvent(AnvilRecipe recipe, ItemStack result) {
        this.recipe = recipe.copy();
        this.result = result.copy();
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
