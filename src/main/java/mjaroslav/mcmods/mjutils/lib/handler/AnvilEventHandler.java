package mjaroslav.mcmods.mjutils.lib.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mjaroslav.mcmods.mjutils.lib.event.AnvilCraftingEvent;
import mjaroslav.mcmods.mjutils.lib.utils.UtilsAnvil;
import mjaroslav.utils.UtilsJava;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;

public class AnvilEventHandler {
    @SubscribeEvent
    public void onAnvilUpdateEvent(AnvilUpdateEvent event) {
        AnvilRecipe recipe = UtilsAnvil.getRecipe(event.left, event.right, UtilsJava.nameFormat(event.name), -1);
        if (recipe != null) {
            ItemStack result = UtilsAnvil.getResult(recipe);
            AnvilCraftingEvent newEvent = new AnvilCraftingEvent(recipe, result);
            if (MinecraftForge.EVENT_BUS.post(newEvent))
                return;
            int count = 0;
            int endSize = 0;
            int lSize = event.left.stackSize;
            int rSize = event.right.stackSize;
            int ingSize = newEvent.recipe.rightStack.stackSize;
            int resSize = newEvent.result.stackSize;
            while (endSize <= newEvent.result.getMaxStackSize())
                if (lSize > 0 && rSize >= ingSize) {
                    count++;
                    endSize += resSize;
                    lSize--;
                    rSize -= ingSize;
                } else
                    break;
            if (count > 0 && event.left.stackSize == count) {
                newEvent.result.stackSize = endSize;
                int cost = count * newEvent.recipe.cost;
                if (cost < 1)
                    cost = 1;
                int materialCost = count * ingSize;
                event.output = newEvent.result;
                event.materialCost = materialCost;
                event.cost = cost;
            }
        }
    }
}
