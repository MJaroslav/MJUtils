package com.github.mjaroslav.mjutils.asm.mixin;

import net.minecraft.util.WeightedRandomFishable;
import net.minecraftforge.common.FishingHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.ArrayList;

@Mixin(value = FishingHooks.class, remap = false)
public interface AccessorFishingHooks {
    @Accessor("fish")
    static ArrayList<WeightedRandomFishable> getFish() {
        throw new AssertionError();
    }

    @Accessor("junk")
    static ArrayList<WeightedRandomFishable> getJunk() {
        throw new AssertionError();
    }

    @Accessor("treasure")
    static ArrayList<WeightedRandomFishable> getTreasure() {
        throw new AssertionError();
    }
}
