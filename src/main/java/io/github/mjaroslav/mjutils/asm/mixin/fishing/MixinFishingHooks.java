package io.github.mjaroslav.mjutils.asm.mixin.fishing;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandom.Item;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraftforge.common.FishingHooks;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;
import java.util.Random;

@Mixin(FishingHooks.class)
public abstract class MixinFishingHooks {
    @SuppressWarnings("rawtypes")
    @Redirect(method = "getRandomFishable(Ljava/util/Random;FII)Lnet/minecraft/item/ItemStack;", at = @At(value =
        "INVOKE", target = "Lnet/minecraft/util/WeightedRandom;getRandomItem(Ljava/util/Random;" +
        "Ljava/util/Collection;)Lnet/minecraft/util/WeightedRandom$Item;"))
    private static @NotNull Item fixNPEWhenListIsEmpty(@NotNull Random rand, @NotNull Collection list) {
        if (list.isEmpty()) return new WeightedRandomFishable(new ItemStack(Blocks.air), 0);
        else return WeightedRandom.getRandomItem(rand, list);
    }
}
