package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.entity.monster.EntityEnderman;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityEnderman.class)
public interface AccessorEntityEnderman {
    @Invoker
    boolean callTeleportTo(double x, double y, double z);
}
