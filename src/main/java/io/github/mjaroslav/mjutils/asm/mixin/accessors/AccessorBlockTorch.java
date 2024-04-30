package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockTorch.class)
public interface AccessorBlockTorch {
    @Invoker("func_150107_m")
    boolean isLowerBlockAvailableForTorch(@NotNull World world, int x, int y, int z);

    @Invoker("func_150108_b")
    boolean isTorchConnectedCorrectly(@NotNull World world, int x, int y, int z, @NotNull Block block);

    @Invoker("func_150109_e")
    boolean tryDropBlockWhenCantStay(@NotNull World world, int x, int y, int z);
}
