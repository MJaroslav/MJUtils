package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockRedstoneTorch.class)
public interface AccessorBlockRedstoneTorch {
    @Invoker("func_150111_a")
    boolean isOverclocking(@NotNull World world, int x, int y, int z, boolean cache);

    @Invoker("func_150110_m")
    boolean isSupportBlockPowered(@NotNull World world, int x, int y, int z);
}
