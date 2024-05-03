package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.entity.boss.EntityWither;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityWither.class)
public interface AccessorEntityWither {
    @Invoker("func_82209_a")
    void throwSkull(int head, double x, double y, double z, boolean invulnerable);
}
