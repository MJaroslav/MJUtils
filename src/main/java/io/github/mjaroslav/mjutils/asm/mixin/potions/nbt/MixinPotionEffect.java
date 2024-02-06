package io.github.mjaroslav.mjutils.asm.mixin.potions.nbt;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PotionEffect.class)
public class MixinPotionEffect {
    @Redirect(method = "writeCustomPotionEffectToNBT", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/nbt/NBTTagCompound;setByte(Ljava/lang/String;B)V", ordinal = 1))
    private void writeCustomPotionEffectToNBT(@NotNull NBTTagCompound instance, @NotNull String name, byte value) {
        instance.setInteger(name, ((PotionEffect) (Object) this).getPotionID());
    }

    /**
     * @author MJUtils (by MJaroslav)
     * @reason Potion IDs extension required patching of reading NBT
     * from PotionEffect too; I no idea how patch this differently
     */
    @Overwrite
    public static @Nullable PotionEffect readCustomPotionEffectFromNBT(@NotNull NBTTagCompound nbt) {
        // Only difference with original method it's getInteger for ID (and type for it) with names of variables
        // I think other patches will work correctly
        var id = nbt.getInteger("Id");
        if (id >= 0 && id < Potion.potionTypes.length && Potion.potionTypes[id] != null) {
            var amplifier = nbt.getByte("Amplifier");
            var duration = nbt.getInteger("Duration");
            var ambient = nbt.getBoolean("Ambient");
            return new PotionEffect(id, duration, amplifier, ambient);
        } else {
            return null;
        }
    }
}
