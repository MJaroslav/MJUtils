package com.github.mjaroslav.mjutils.hook;

import com.github.mjaroslav.mjutils.util.UtilsFishing;
import com.google.common.base.Predicate;
import com.github.mjaroslav.mjutils.gloomyfolken.hooklib.asm.Hook;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraftforge.common.FishingHooks;

import static com.github.mjaroslav.mjutils.gloomyfolken.hooklib.asm.ReturnCondition.ALWAYS;
import static net.minecraftforge.common.FishingHooks.FishableCategory.*;

// TODO: Решить, необходимо ли это или нет
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
    public static void clinit(FishingHooks instance) {
        // IGNORED
    }
}
