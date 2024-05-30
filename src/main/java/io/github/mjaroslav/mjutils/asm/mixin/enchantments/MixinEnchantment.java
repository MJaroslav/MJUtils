package io.github.mjaroslav.mjutils.asm.mixin.enchantments;

import io.github.mjaroslav.mjutils.asm.MixinPatches.Enchantments;
import io.github.mjaroslav.mjutils.internal.common.modular.IDManagerModule;
import io.github.mjaroslav.mjutils.lib.MJUtilsInfo;
import net.minecraft.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(Enchantment.class)
public abstract class MixinEnchantment {
    @Mutable
    @Shadow
    public static @Final Enchantment[] enchantmentsList;

    @Mutable
    @Shadow
    public @Final int effectId;

    // Handle ID duplicates
    @ModifyVariable(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/enchantment/Enchantment;effectId:I",
        shift = Shift.BEFORE, ordinal = 0), ordinal = 0, argsOnly = true)
    private int mjtuils$init(int original) {
        return IDManagerModule.ENCHANTMENTS.registerId(getClass(), original);
    }

    // Extends enchantments array.
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void mjtuils$cinit(@NotNull CallbackInfo ci) {
        enchantmentsList = Arrays.copyOf(enchantmentsList, Enchantments.newArraySize);
        MJUtilsInfo.LOG_LIB.debug("Enchantments array size changed to {}", Enchantments.newArraySize);
    }
}
