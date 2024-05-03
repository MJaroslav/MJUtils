package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.entity.monster.EntityGhast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityGhast.class)
public interface AccessorEntityGhast {
    @Invoker
    boolean callIsCourseTraversable(double x, double y, double z, double distance);
}
