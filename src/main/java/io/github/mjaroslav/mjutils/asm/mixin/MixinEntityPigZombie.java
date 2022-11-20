package io.github.mjaroslav.mjutils.asm.mixin;

import io.github.mjaroslav.mjutils.event.PigZombieBecomeAngryEvent;
import lombok.val;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPigZombie.class)
public class MixinEntityPigZombie extends EntityZombie {
    public MixinEntityPigZombie() {
        super(null);
    }

    @Inject(method = "becomeAngryAt", at = @At("HEAD"), cancellable = true)
    public void inject(@Nullable Entity entity, @NotNull CallbackInfo ci) {
        val event = new PigZombieBecomeAngryEvent((EntityPigZombie) (Object) this, entity);
        if (MinecraftForge.EVENT_BUS.post(event)) ci.cancel();
    }
}
