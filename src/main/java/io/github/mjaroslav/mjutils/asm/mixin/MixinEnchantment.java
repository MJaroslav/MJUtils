package io.github.mjaroslav.mjutils.asm.mixin;

import net.minecraft.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(Enchantment.class)
public abstract class MixinEnchantment {
    @Mutable
    @Shadow
    private static Enchantment[] enchantmentsList;

    // Extends enchantments array.
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void cinit(CallbackInfo ci) {
        enchantmentsList = Arrays.copyOf(enchantmentsList, 1024);
    }
}
