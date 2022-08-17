package com.github.mjaroslav.mjutils.asm.mixin;

import com.github.mjaroslav.mjutils.mod.lib.CategoryRoot;
import com.github.mjaroslav.mjutils.util.game.world.UtilsWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityTNTPrimed.class)
public abstract class MixinEntityTNTPrimed extends Entity {
    public MixinEntityTNTPrimed(World world) {
        super(world);
    }

    // Configurable no loss explosion
    @Inject(method = "explode", at = @At("HEAD"), cancellable = true)
    private void explode(CallbackInfo ci) {
        if (CategoryRoot.noLossTNTExplosion)
            UtilsWorld.newExplosionWithDropChance(worldObj, this, posX, posY, posZ, 4F, false, true, 1F);
        else
            worldObj.createExplosion(this, posX, posY, posZ, 4F, true);
        ci.cancel();
    }
}
