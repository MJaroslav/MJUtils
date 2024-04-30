package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityPigZombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityPigZombie.class)
public interface AccessorEntityPigZombie {
    @Invoker
    void callBecomeAngryAt(Entity target);
}
