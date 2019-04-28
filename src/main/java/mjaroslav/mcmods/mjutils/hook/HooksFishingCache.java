package mjaroslav.mcmods.mjutils.hook;

import com.google.common.base.Predicate;
import mjaroslav.mcmods.mjutils.gloomyfolken.hooklib.asm.Hook;
import mjaroslav.mcmods.mjutils.lib.util.UtilsFishing;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraftforge.common.FishingHooks;

import java.util.Random;

import static mjaroslav.mcmods.mjutils.gloomyfolken.hooklib.asm.ReturnCondition.ALWAYS;
import static net.minecraftforge.common.FishingHooks.FishableCategory.*;

public class HooksFishingCache {
    public static final String DISABLE_ID = "hooks_fishing_cache";

    @Hook(returnCondition = ALWAYS)
    public static void addFish(FishingHooks instance, WeightedRandomFishable item) {
        UtilsFishing.add(item, FISH);
    }

    @Hook(returnCondition = ALWAYS)
    public static void addJunk(FishingHooks instance, WeightedRandomFishable item) {
        UtilsFishing.add(item, JUNK);
    }

    @Hook(returnCondition = ALWAYS)
    public static void addTreasure(FishingHooks instance, WeightedRandomFishable item) {
        UtilsFishing.add(item, TREASURE);
    }

    @Hook(returnCondition = ALWAYS)
    public static void removeFish(FishingHooks instance, Predicate<WeightedRandomFishable> test) {
        UtilsFishing.remove(test, FISH);
    }

    @Hook(returnCondition = ALWAYS)
    public static void removeJunk(FishingHooks instance, Predicate<WeightedRandomFishable> test) {
        UtilsFishing.remove(test, JUNK);
    }

    @Hook(returnCondition = ALWAYS)
    public static void removeTreasure(FishingHooks instance, Predicate<WeightedRandomFishable> test) {
        UtilsFishing.remove(test, TREASURE);
    }

    @Hook(returnCondition = ALWAYS, targetMethod = "<clinit>")
    public static void removeTreasure(FishingHooks instance) {
    }

    @Hook(returnCondition = ALWAYS)
    public static ItemStack getRandomFishable(FishingHooks instance, Random rand, float chance, int luck, int speed) {
        float junkChance = 0.1F - luck * 0.025F - speed * 0.01F;
        float treasureChance = 0.05F + luck * 0.01F - speed * 0.01F;
        junkChance = MathHelper.clamp_float(junkChance, 0.0F, 1.0F);
        treasureChance = MathHelper.clamp_float(treasureChance, 0.0F, 1.0F);
        if (chance < junkChance)
            return ((WeightedRandomFishable) WeightedRandom.getRandomItem(rand,
                    UtilsFishing.getCategory(JUNK))).func_150708_a(rand);
        chance -= junkChance;
        if (chance < treasureChance)
            return ((WeightedRandomFishable) WeightedRandom.getRandomItem(rand,
                    UtilsFishing.getCategory(TREASURE))).func_150708_a(rand);
        return ((WeightedRandomFishable) WeightedRandom.getRandomItem(rand,
                UtilsFishing.getCategory(FISH))).func_150708_a(rand);
    }
}
