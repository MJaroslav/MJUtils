package io.github.mjaroslav.mjutils.asm.mixin;

import io.github.mjaroslav.mjutils.internal.lib.ModInfo;
import net.minecraft.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
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
    public static @Final Enchantment[] enchantmentsList;

    // Extends enchantments array.
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void cinit(@NotNull CallbackInfo ci) {
        enchantmentsList = Arrays.copyOf(enchantmentsList, 1024);
        ModInfo.loggerLibrary.debug("Enchantments array size changed to 1024");
    }
}
