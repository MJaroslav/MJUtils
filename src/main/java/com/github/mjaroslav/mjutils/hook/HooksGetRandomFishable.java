package com.github.mjaroslav.mjutils.hook;

import com.github.mjaroslav.mjutils.util.UtilsFishing;
import com.github.mjaroslav.mjutils.gloomyfolken.hooklib.asm.Hook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraftforge.common.FishingHooks;

import java.util.Random;

import static com.github.mjaroslav.mjutils.gloomyfolken.hooklib.asm.ReturnCondition.ALWAYS;
import static net.minecraftforge.common.FishingHooks.FishableCategory.*;

public class HooksGetRandomFishable {
    @Hook(returnCondition = ALWAYS)
    public static ItemStack getRandomFishable(FishingHooks instance, Random rand, float chance, int luck, int speed) {
        float junkChance = 0.1F - luck * 0.025F - speed * 0.01F;
        float treasureChance = 0.05F + luck * 0.01F - speed * 0.01F;
        junkChance = MathHelper.clamp_float(junkChance, 0.0F, 1.0F);
        treasureChance = MathHelper.clamp_float(treasureChance, 0.0F, 1.0F);
        // Empty fix
        if (chance < junkChance && !UtilsFishing.getCategory(JUNK).isEmpty())
            return ((WeightedRandomFishable) WeightedRandom.getRandomItem(rand,
                    UtilsFishing.getCategory(JUNK))).func_150708_a(rand);
        chance -= junkChance;
        if (chance < treasureChance && !UtilsFishing.getCategory(TREASURE).isEmpty())
            return ((WeightedRandomFishable) WeightedRandom.getRandomItem(rand,
                    UtilsFishing.getCategory(TREASURE))).func_150708_a(rand);
        if (UtilsFishing.getCategory(FISH).isEmpty())
            return null;
        else
            return ((WeightedRandomFishable) WeightedRandom.getRandomItem(rand,
                    UtilsFishing.getCategory(FISH))).func_150708_a(rand);
    }
}
