package io.github.mjaroslav.mjutils.asm.mixin.potions;

import io.github.mjaroslav.mjutils.asm.MixinPatches.Potions;
import io.github.mjaroslav.mjutils.internal.common.modular.IDManagerModule;
import io.github.mjaroslav.mjutils.lib.MJUtilsInfo;
import net.minecraft.potion.Potion;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(Potion.class)
public abstract class MixinPotion {
    @Mutable
    @Shadow
    public static @Final Potion[] potionTypes;

    @Mutable
    @Shadow
    public @Final int id;

    // Handle ID duplicates
    @ModifyVariable(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/potion/Potion;id:I",
        shift = Shift.BEFORE, ordinal = 0), ordinal = 0)
    private int init(int original) {
        return IDManagerModule.POTIONS.registerId(getClass(), original);
    }

    @Inject(method = "setPotionName", at = @At("HEAD"))
    private void inject$addNameForReiteration(@NotNull String name, @NotNull CallbackInfoReturnable<Potion> ci) {
        IDManagerModule.POTIONS.setComment(id, name);
    }

    // Extends potions array.
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void cinit(@NotNull CallbackInfo ci) {
        potionTypes = Arrays.copyOf(potionTypes, Potions.newArraySize);
        MJUtilsInfo.LOG_LIB.debug("Potions array size changed to {}", Potions.newArraySize);
    }
}
