package com.github.mjaroslav.mjutils.asm.mixin;

import net.minecraft.potion.Potion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(Potion.class)
public abstract class MixinPotion {
    @Mutable
    @Shadow
    private static @Final Potion[] potionTypes;

    @Mutable
    @Shadow
    private @Final int id;

    // Cause crash when something trying register potion with already registered id.
    @Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/potion/Potion;id:I",
            ordinal = 0))
    private void injected(Potion p, int id) {
        if (potionTypes[id] != null)
            throw new IllegalArgumentException("Duplicate potion id! " + getClass() + " and " +
                    potionTypes[id].getClass() + " Enchantment ID:" + id);
        this.id = id;
    }

    // Extends potions array.
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void cinit(CallbackInfo ci) {
        potionTypes = Arrays.copyOf(potionTypes, 1024);
    }
}
