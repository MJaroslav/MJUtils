package io.github.mjaroslav.mjutils.asm.mixin;

import io.github.mjaroslav.mjutils.mod.lib.General;
import io.github.mjaroslav.mjutils.util.game.world.UtilsWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityCreeper.class)
public abstract class MixinEntityCreeper extends EntityMob {
    public MixinEntityCreeper() {
        super(null);
    }

    // Configurable no loss explosion
    @Redirect(remap = false, method = "func_146077_cc", at = @At(value = "INVOKE", target =
        "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFZ)Lnet/minecraft/world/Explosion;"))
    private Explosion explode(@NotNull World instance, @NotNull Entity owner, double x, double y, double z, float power,
                              boolean isFlaming) {
        if (General.overrideCreeperExplosionDropChance > 0)
            return UtilsWorld.newExplosionWithDropChance(worldObj, owner, x, y, z, power, false, isFlaming,
                (float) General.overrideCreeperExplosionDropChance);
        else return worldObj.createExplosion(owner, x, y, z, power, isFlaming);
    }
}
