package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockButton;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockButton.class)
public interface AccessorBlockButton {
    @Invoker("func_150045_e")
    int getRotatedMetaByNeighbors(@NotNull World world, int x, int y, int z);

    @Invoker("func_150044_m")
    boolean checkAndDropBlock(@NotNull World world, int x, int y, int z);

    @Invoker("func_150046_n")
    void updateAndPlayEffects(@NotNull World world, int x, int y, int z);

    @Invoker("func_150042_a")
    void notifyBlocksOfNeighborChange(@NotNull World world, int x, int y, int z, int meta);
}
