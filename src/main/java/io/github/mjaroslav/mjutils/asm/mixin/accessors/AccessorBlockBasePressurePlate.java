package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockBasePressurePlate.class)
public interface AccessorBlockBasePressurePlate {
    @Invoker("func_150065_e")
    int getRedstonePowerByCollidedEntities(@NotNull World world, int x, int y, int z);

    @Invoker("func_150062_a")
    void updateAndPlayEffects(@NotNull World world, int x, int y, int z, int meta);

    @Invoker("func_150064_a_")
    void notifyBlocksOfNeighborChange(@NotNull World world, int x, int y, int z);
}
