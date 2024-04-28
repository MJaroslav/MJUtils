package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockRailBase.class)
public interface AccessorBlockRailBase {
    @Invoker("func_150048_a")
    void onRailNeighborBlockChange(@NotNull World p_150048_1_, int x, int y, int z, int meta, int baseMeta,
                                   @NotNull Block block);

    @Invoker("func_150052_a")
    void updateRailBaseBlockState(@NotNull World world, int x, int y, int z, boolean forceUpdate);
}
