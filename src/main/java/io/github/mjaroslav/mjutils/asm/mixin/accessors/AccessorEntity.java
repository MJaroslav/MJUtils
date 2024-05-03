package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface AccessorEntity {
    @Invoker("func_145780_a")
    void playStepSound(int x, int y, int z, @NotNull Block block);

    @Invoker("func_145771_j")
    boolean tryCorrectMotionOnBlockCollision(double x, double y, double z);
}
