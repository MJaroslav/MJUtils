package io.github.mjaroslav.mjutils.asm.mixin;

import io.github.mjaroslav.mjutils.internal.lib.General;
import io.github.mjaroslav.mjutils.util.game.world.UtilsWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityTNTPrimed.class)
public abstract class MixinEntityTNTPrimed extends Entity {
    private MixinEntityTNTPrimed() {
        super(null);
    }

    // Configurable no loss explosion
    @Inject(method = "explode", at = @At("HEAD"), cancellable = true)
    private void explode(@NotNull CallbackInfo ci) {
        if (General.overrideTNTExplosionDropChance > 0)
            UtilsWorld.newExplosionWithDropChance(worldObj, this, posX, posY, posZ, 4F, false, true,
                (float) General.overrideTNTExplosionDropChance);
        else if (General.overrideTNTExplosionDropChance == -1)
            worldObj.createExplosion(this, posX, posY, posZ, 4F, true);
        ci.cancel();
    }
}
