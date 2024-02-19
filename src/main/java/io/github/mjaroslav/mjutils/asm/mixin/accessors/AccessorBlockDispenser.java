package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockDispenser;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockDispenser.class)
public interface AccessorBlockDispenser {
    @Invoker("func_149938_m")
    void rotateBlockToFreeSide(@NotNull World world, int x, int y, int z);

    @Invoker("func_149941_e")
    void updateAndPlayEffects(@NotNull World world, int x, int y, int z);
}
