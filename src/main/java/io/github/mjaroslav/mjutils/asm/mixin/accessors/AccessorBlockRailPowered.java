package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockRailPowered;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockRailPowered.class)
public interface AccessorBlockRailPowered {
    @Invoker("func_150058_a")
    boolean isNeighborRailsPowered(@NotNull World world, int x, int y, int z, int baseMeta, boolean reverse,
                                   int distance);

    @Invoker("func_150057_a")
    boolean isRailPowered(@NotNull World world, int x, int y, int z, boolean reverse, int distance, int baseMeta);
}
