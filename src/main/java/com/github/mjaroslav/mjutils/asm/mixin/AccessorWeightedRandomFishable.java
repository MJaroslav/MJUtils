package com.github.mjaroslav.mjutils.asm.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomFishable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WeightedRandomFishable.class)
public interface AccessorWeightedRandomFishable {
    @Accessor(value = "field_150711_b", remap = false)
    ItemStack getFishableStack();

    @Accessor(value = "field_150712_c", remap = false)
    float getRandomDamage();

    @Accessor(value = "field_150710_d", remap = false)
    boolean getEnchantable();
}
